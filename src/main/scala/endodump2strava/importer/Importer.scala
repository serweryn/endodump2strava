package endodump2strava.importer

import akka.actor.ActorSystem
import com.typesafe.scalalogging.{CanLog, LazyLogging, Logger}
import endodump2strava.db.{ImportedActivity, ImportedActivityStep, Queries, TokenInfo}
import endodump2strava.endo.{Workout, WorkoutFileType}
import endodump2strava.importer.Implicits.RichFuture
import endodump2strava.importer.Importer.getConfigString
import endodump2strava.strava.api.OAuthApi
import io.getquill.{H2JdbcContext, SnakeCase}
import io.swagger.client.core.{ApiError, ApiInvoker, ApiResponse}
import org.slf4j.MDC
import play.api.libs.json.Json

import java.io.{File, FileInputStream, FilenameFilter}
import scala.concurrent.{blocking, ExecutionContext, Future}
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

class Importer(implicit system: ActorSystem) extends LazyLogging {
  case class NameAndFilename(name: String, filename: String)

  private implicit case object CanLogNameAndFilename extends CanLog[NameAndFilename] {
    val correlationId  = "correlationId"
    override def logMessage(originalMsg: String, a: NameAndFilename): String = {
      MDC.put(correlationId, a.name)
      originalMsg
    }
    override def afterLog(a: NameAndFilename): Unit = MDC.remove(correlationId)
  }

  private val ctxLogger = Logger.takingImplicit[NameAndFilename]("ctx-logger")

  private implicit val ec: ExecutionContext = system.dispatcher
  private val invoker = new GuardedApiInvoker(ApiInvoker())

  private val user = "serweryn"
  private val endoDirname = getConfigString("endo-workouts-dir")
  private val sqlCtx = new H2JdbcContext[SnakeCase](SnakeCase, "endodump2strava.db")
  system.registerOnTermination(sqlCtx.close())

  private val db = new Queries(sqlCtx)

  def doImport(): Unit = {
    val allWorkouts = allEndoWorkouts()
    val completedWorkouts = db.completedActivities().map(_.workoutBasename).toSet
    val notCompletedWorkouts = allWorkouts.filterNot(x => completedWorkouts.contains(x.name)).sortBy(_.name)(Ordering[String].reverse)

    logger.info(s"already completed ${completedWorkouts.size} of ${allWorkouts.size}, resuming...")

    if (notCompletedWorkouts.isEmpty) return

    invoker.reset()

    accessToken().map { token =>
      val stravaApi = new RequestCreator(token)
      notCompletedWorkouts.foreach { x =>
        if (invoker.valid) {
          val workoutImporter = new SingleWorkoutImporter(stravaApi)(x)
          workoutImporter.doImport()
          blocking {
            Thread.sleep(5.seconds.toMillis)
          }
        }
      }
      logger.info("done for this round...")
    }
  }

  private def accessToken(): Future[String] = {
    val clientId = getConfigString("client-id")
    val clientSecret = getConfigString("client-secret")
    val refreshToken = db.selectTokenInfo(user).map(_.refreshToken)
      .headOption.getOrElse(getConfigString("refresh-token"))

    logger.info("refreshing access_token")

    val request = OAuthApi.refreshToken(clientId, clientSecret, refreshToken)
    val response = invoker.execute(request)

    response.map { apiResponse =>
      val ati = apiResponse.content
      sqlCtx.transaction {
        db.deleteTokenInfo(user)
        db.insertTokenInfo(TokenInfo(user, ati.tokenType.get, ati.accessToken.get, ati.expiresAt.get, ati.refreshToken.get))
      }
      ati.accessToken.get
    }
  }

  private def allEndoWorkouts(): Seq[NameAndFilename] = {
    val endoDir = new File(endoDirname)
    if (!endoDir.exists() || !endoDir.isDirectory)
      throw new IllegalArgumentException(s"$endoDirname does not exist or is not a directory")

    val `.tcx.gz` = "." + WorkoutFileType.TcxGz.extension
    val tcxFiles = endoDir.listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = name.endsWith(`.tcx.gz`)
    }).toSeq
    tcxFiles.foreach { f =>
      val jsonFname = f.getAbsolutePath.replace(WorkoutFileType.TcxGz.extension, WorkoutFileType.Json.extension)
      if (! new File(jsonFname).exists()) throw new IllegalStateException(s"$jsonFname does not exist")
    }

    tcxFiles.map(f => NameAndFilename(f.getName.replace(`.tcx.gz`, ""), f.getAbsolutePath))
  }

  class SingleWorkoutImporter(stravaApi: RequestCreator)(implicit metadata: NameAndFilename) {

    def doImport(): Future[Unit] = {
      ctxLogger.info("starting import...")
      val f = uploadWorkout() flatMap { uploadId =>
        getActivityId(uploadId) flatMap { activityId =>
          updateActivity(activityId)
        }
      }
      f andThen {
        case Success(_) => ctxLogger.info("successful import")
        case Failure(e) => ctxLogger.warn(s"error during import: ${e.getMessage}")
      }
    }

    private def uploadWorkout(): Future[Long] = {
      val activity = db.selectActivity(metadata.name).headOption
      if (activity.nonEmpty) {
        ctxLogger.info("activity already uploaded")
        Future.successful(activity.get.uploadId)
      } else {
        ctxLogger.info("starting upload...")
        db.deleteActivityStep(metadata.name, ImportedActivityStep.createUpload)
        db.deleteActivity(metadata.name)
        val req = stravaApi.createUpload(metadata.filename, metadata.name, WorkoutFileType.TcxGz.extension)
        val res = invoker.execute(req)
        val sleepAfterRequest = 20.seconds
        res map {
          case ApiResponse(_, body, _) => body.id.get
        } andThen {
          case Success(id) => db.insertActivity(ImportedActivity(metadata.name, id, None))
        } andThen {
          case _ => saveActivityStep(res, ImportedActivityStep.createUpload)
        } andThen {
          case Success(id) => ctxLogger.info(s"got uploadId=$id, sleeping for $sleepAfterRequest before getting activityId...")
        } sleep {
          sleepAfterRequest
        }
      }
    }

    private def saveActivityStep[A](f: Future[ApiResponse[A]], stepName: String): Future[ApiResponse[A]] = {
      f andThen { case a =>
        val (code, body, headers) = a match {
          case Success(v) => (v.code, "", "")
          case Failure(ApiError(code, msg, content, _, headers)) => (code, s"$msg: $content", headers.toString)
          case Failure(e) => (ErrorCodes.OtherApiError, s"${e.getMessage}", "")
        }
        db.insertActivityStep(ImportedActivityStep(metadata.name, stepName, code, body, headers))
      }
    }

    private def getActivityId(uploadId: Long): Future[Long] = {
      val activity = db.selectActivity(metadata.name).head
      val step = db.selectActivityStep(metadata.name, ImportedActivityStep.getUpload).headOption
      if (activity.activityId.nonEmpty) {
        ctxLogger.info("activityId already retrieved")
        Future.successful(activity.activityId.get)
      } else if (step.nonEmpty && successfulResponseCode(step.get.responseCode)) {
        Future.failed(new IllegalStateException("ignored because of override"))
      } else {
        ctxLogger.info("getting activityId...")
        db.deleteActivityStep(metadata.name, ImportedActivityStep.getUpload)
        val req = stravaApi.getUpload(uploadId)
        val res = invoker.execute(req)
        res flatMap {
          case r @ ApiResponse(_, body, headers) =>
            if (body.activityId.nonEmpty) Future.successful(r)
            else {
              def get(o: Option[String]) = o.getOrElse("got empty from Strava")
              val status = get(body.status)
              val error = get(body.error)
              val msg = s"$status: $error"
              def apiError(code: Int) = Future.failed(ApiError(code, msg, Option(body), headers = headers))

              // for duplicates and empty files use "successful" code (less than 300) to not repeat them in next runs
              if (error.contains("duplicate of activity")) apiError(297)
              else if (error.contains("The file is empty")) apiError(298)
              else Future.failed(new IllegalStateException(msg))
            }
        } andThen {
          case t => saveActivityStep(Future.fromTry(t), ImportedActivityStep.getUpload)
        } map {
          case ApiResponse(_, body, _) =>
            val activityId = body.activityId.get
            ctxLogger.info(s"got activityId=$activityId")
            db.updateActivity(activity.copy(activityId = Option(activityId)))
            activityId
        }
      }
    }

    private def updateActivity(activityId: Long): Future[Unit] = {
      val step = db.selectActivityStep(metadata.name, ImportedActivityStep.updateActivity).headOption
      if (step.nonEmpty && successfulResponseCode(step.get.responseCode)) {
        ctxLogger.info("activity already updated")
        Future.successful(())
      } else {
        ctxLogger.info("updating activity...")
        db.deleteActivityStep(metadata.name, ImportedActivityStep.updateActivity)
        val workoutJsonFilename = s"$endoDirname/${metadata.name}.${WorkoutFileType.Json.extension}"
        val workoutJson = Json.parse(new FileInputStream(workoutJsonFilename))
        val workout = Workout(workoutJson)
        val req = stravaApi.updateActivity(activityId, workout)
        val res = invoker.execute(req)
        res andThen {
          case _ => saveActivityStep(res, ImportedActivityStep.updateActivity)
        } flatMap {
          case ApiResponse(code, _, _) =>
            if (successfulResponseCode(code)) Future.successful(())
            else Future.failed(new IllegalStateException("got error response code for updateActivity"))
        }
      }
    }

    private def successfulResponseCode(code: Int) = {
      code < 300
    }
  }

}

object Importer {
  def getConfigString(suffix: String)(implicit system: ActorSystem): String =
    system.settings.config.getString("endodump2strava." + suffix)
}

package endodump2strava.importer

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import endodump2strava.db.{ImportedActivity, ImportedActivityStep, Queries, TokenInfo}
import endodump2strava.endo.WorkoutFileType
import endodump2strava.importer.Implicits.RichFuture
import endodump2strava.importer.Importer.getConfigString
import endodump2strava.strava.api.OAuthApi
import io.getquill.{H2JdbcContext, SnakeCase}
import io.swagger.client.core.{ApiError, ApiInvoker, ApiResponse}

import java.io.{File, FilenameFilter}
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

class Importer(implicit system: ActorSystem) extends LazyLogging {

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

    if (notCompletedWorkouts.isEmpty) return

    accessToken().map { token =>
      val stravaApi = new RequestCreator(token)
      val workoutImporter = new WorkoutImporter(stravaApi)

      workoutImporter.importSingle(notCompletedWorkouts.head)
    }
  }

  private def accessToken(): Future[String] = {
    val clientId = getConfigString("client-id")
    val clientSecret = getConfigString("client-secret")
    val refreshToken = db.selectTokenInfo(user).map(_.refreshToken)
      .headOption.getOrElse(getConfigString("refresh-token"))

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

  case class NameAndFilename(name: String, filename: String)

  def allEndoWorkouts(): Seq[NameAndFilename] = {
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

  class WorkoutImporter(stravaApi: RequestCreator) {
    def importSingle(metadata: NameAndFilename): Future[Unit] = {
      uploadWorkout(metadata) flatMap { uploadId =>
        getActivityId(uploadId) flatMap { activityId =>
          updateActivity(activityId, metadata)
        }
      }
    }

    private def uploadWorkout(meta: NameAndFilename): Future[Long] = {
      val activity = db.selectActivity(meta.name).headOption
      if (activity.nonEmpty) Future.successful(activity.get.uploadId)
      else {
        db.deleteActivityStep(meta.name, ImportedActivityStep.createUpload)
        db.deleteActivity(meta.name)
        val req = stravaApi.createUpload(meta.filename, meta.name, WorkoutFileType.TcxGz.extension)
        val res = invoker.execute(req)
        val sleepAfterRequest = 10.seconds
        res map {
          case ApiResponse(_, body, _) => body.id.get
        } andThen {
          case Success(id) => db.insertActivity(ImportedActivity(meta.name, id, None))
        } andThen {
          case _ => saveActivityStep(res, meta.name, ImportedActivityStep.createUpload)
        } andThen {
          case _ => logger.info(s"Sleeping for $sleepAfterRequest...")
        } sleep {
          sleepAfterRequest
        }
      }
    }

    private def saveActivityStep[A](f: Future[ApiResponse[A]], workoutBasename: String, stepName: String): Future[ApiResponse[A]] = {
      f andThen { case a =>
        val (code, body, headers) = a match {
          case Success(v) => (v.code, v.content.toString, v.headers.toString)
          case Failure(ApiError(code, msg, content, _, headers)) => (code, s"$msg: $content", headers.toString)
          case Failure(e) => (498, s"${e.getMessage}: ${e.getStackTrace.toString}", "")
        }
        db.insertActivityStep(ImportedActivityStep(workoutBasename, stepName, code, body, headers))
      }
    }

    private def getActivityId(uploadId: Long): Future[Long] = ???

    private def updateActivity(activityId: Long, metadata: NameAndFilename): Future[Unit] = ???
  }

}

object Importer {
  def getConfigString(suffix: String)(implicit system: ActorSystem): String =
    system.settings.config.getString("endodump2strava." + suffix)
}

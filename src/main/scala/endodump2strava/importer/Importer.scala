package endodump2strava.importer

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import endodump2strava.db.{Queries, TokenInfo}
import endodump2strava.endo.WorkoutFileType
import endodump2strava.importer.Importer.getConfigString
import endodump2strava.strava.api.OAuthApi
import io.getquill.{H2JdbcContext, SnakeCase}
import io.swagger.client.core.ApiInvoker

import java.io.{File, FilenameFilter}
import scala.concurrent.{ExecutionContext, Future}

class Importer(implicit system: ActorSystem) extends LazyLogging {

  private implicit val ec: ExecutionContext = system.dispatcher
  private val invoker = new GuardedApiInvoker(ApiInvoker())

  private val user = "serweryn"
  private val sqlCtx = new H2JdbcContext[SnakeCase](SnakeCase, "endodump2strava.db")
  system.registerOnTermination(sqlCtx.close())

  private val db = new Queries(sqlCtx)

  def doImport(): Unit = {
    val allWorkouts = allEndoWorkouts()
    val completedWorkouts = db.completedActivities().map(_.workoutBasename).toSet
    val notCompletedWorkouts = allWorkouts.filterNot(x => completedWorkouts.contains(x.name))

    if (notCompletedWorkouts.isEmpty) return

    accessToken().map { token =>
      val stravaApi = new RequestCreator(token)
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
    val endoDirname = getConfigString("endo-workouts-dir")
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

}

object Importer {
  def getConfigString(suffix: String)(implicit system: ActorSystem): String =
    system.settings.config.getString("endodump2strava." + suffix)
}

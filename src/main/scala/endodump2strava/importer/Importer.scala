package endodump2strava.importer

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import endodump2strava.db.Queries
import endodump2strava.importer.Importer.getConfigString
import endodump2strava.strava.api.OAuthApi
import io.getquill.{H2JdbcContext, SnakeCase}
import io.swagger.client.core.ApiInvoker

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class Importer(implicit system: ActorSystem) extends LazyLogging {

  private implicit val invoker: ApiInvoker = ApiInvoker()

  private val user = "serweryn"
  private val sqlCtx = new H2JdbcContext[SnakeCase](SnakeCase, "endodump2strava.db")
  system.registerOnTermination(sqlCtx.close())

  private val queries = new Queries(sqlCtx)

  private def o[A](a: A) = Option(a)

  def doImport(): Unit = {
    val token = accessToken()
    token.map { token =>
      logger.info(s"$token")
    }
  }

  def accessToken(): Future[String] = {
    val clientId = getConfigString("client-id")
    val clientSecret = getConfigString("client-secret")
    val refreshToken = queries.selectRefreshToken(user).map(_.refreshToken)
      .headOption.getOrElse(getConfigString("refresh-token"))

    val request = OAuthApi.refreshToken(clientId, clientSecret, refreshToken)
    val response = invoker.execute(request)

    response.map { apiResponse =>
      val ati = apiResponse.content
      queries.deleteTokenInfo(user)
      queries.insertTokenInfo(user, ati.tokenType.get, ati.accessToken.get, ati.expiresAt.get, ati.refreshToken.get)
      ati.accessToken.get
    }
  }

}

object Importer {
  def getConfigString(suffix: String)(implicit system: ActorSystem) =
    system.settings.config.getString("endodump2strava." + suffix)
}

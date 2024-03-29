package endodump2strava.importer

import java.io.{File, FileInputStream}
import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import endodump2strava.endo.{Sport, Workout}
import endodump2strava.strava.api.{ActivitiesApi, OAuthApi, UploadsApi}
import endodump2strava.db.{Queries, TokenInfo}
import endodump2strava.importer.Importer.getConfigString
import endodump2strava.strava.model.UpdatableActivity
import io.getquill.{H2JdbcContext, SnakeCase}
import io.swagger.client.core.{ApiInvoker, ApiKeyLocations, ApiKeyValue, ApiResponse}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Trial extends App with LazyLogging {

  implicit val system: ActorSystem = ActorSystem()
  implicit val invoker: ApiInvoker = ApiInvoker()
  val user = "serweryn"

  val ctx = new H2JdbcContext[SnakeCase](SnakeCase, "endodump2strava.db")
  system.registerOnTermination(ctx.close())

  val q = new Queries(ctx)

  def o[A](a: A) = Option(a)

//  val request = {
////    createUpload()
//    createUpdateActivity()
//  }

  val clientId = getConfigString("client-id")
  val clientSecret = getConfigString("client-secret")
  val refreshToken = q.selectTokenInfo(user).map(_.refreshToken)
    .headOption.getOrElse(getConfigString("refresh-token"))

  val request = OAuthApi.refreshToken(clientId, clientSecret, refreshToken)
  val requestWithAuth = request

//  val requestWithAuth = request
//    .withApiKey(ApiKeyValue(s"Bearer $accessToken"), "Authorization", ApiKeyLocations.HEADER)

  val response = invoker.execute(requestWithAuth)
  response onComplete {
    case Success(apiResponse) =>
      val ati = apiResponse.content
      q.deleteTokenInfo(user)
      q.insertTokenInfo(TokenInfo(user, ati.tokenType.get, ati.accessToken.get, ati.expiresAt.get, ati.refreshToken.get))
    case Failure(e) =>
      logger.error(s"$e")
  }

  private def createUpload() = {
    UploadsApi.createUpload(
      o(new File("C:\\Users\\seweryn\\sport\\endomondo\\Workouts\\2021-01-02 11_03_27.0.tcx.gz")),
      name = o("2021-01-02 11_03_27.0"),
      dataType = o("tcx.gz"),
      externalId = o("2021-01-02 11_03_27.0")
    )
  }

  private def createUpdateActivity() = {
    val workoutJsonFile = new File("C:\\Users\\seweryn\\sport\\endomondo\\Workouts\\2021-01-02 11_03_27.0.json")
    val workoutJson = Json.parse(new FileInputStream(workoutJsonFile))
    val workout = Workout(workoutJson)
    val sport = Sport.withName(workout.sport)
    val activity = Sport2Activity(sport)

    logger.info(s"$workout")

    ActivitiesApi.updateActivityById(
      id = 4676223428L,
      o(UpdatableActivity(
        `type` = o(activity.activityType.entryName),
        commute = o(activity.commute),
        trainer = o(activity.trainer),
        description = workout.message,
        name = workout.name
      ))
    ).copy[UpdatableActivity](contentType = "application/json; charset=utf-8")
  }

}

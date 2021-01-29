package endodump2strava.importer

import endodump2strava.endo.{Sport, Workout}
import endodump2strava.strava.api.{ActivitiesApi, UploadsApi}
import endodump2strava.strava.model.{DetailedActivity, UpdatableActivity, Upload}
import io.swagger.client.core.{ApiKeyLocations, ApiKeyValue, ApiRequest}

import java.io.File

class RequestCreator(accessToken: String) {

  private def o[A](a: A) = Option(a)

  def createUpload(filename: String, name: String, dataType: String): ApiRequest[Upload] = {
    UploadsApi.createUpload(
      file = o(new File(filename)),
      name = o(name),
      dataType = o(dataType),
      externalId = o(name))
      .addApiKey()
  }

  def getUpload(uploadId: Long): ApiRequest[Upload] =
    UploadsApi.getUploadById(uploadId).addApiKey()

  def updateActivity(activityId: Long, workout: Workout): ApiRequest[DetailedActivity] = {
    val sport = Sport.withName(workout.sport)
    val activity = Sport2Activity(sport)

    ActivitiesApi.updateActivityById(
      id = activityId,
      o(UpdatableActivity(
        `type` = o(activity.activityType.entryName),
        commute = o(activity.commute),
        trainer = o(activity.trainer),
        description = workout.message,
        name = workout.name)))
      .withApplicationJsonUtf8()
      .addApiKey()
  }

  implicit class RichApiRequest[A](req: ApiRequest[A]) {
    def addApiKey(): ApiRequest[A] =
      req.withApiKey(ApiKeyValue(s"Bearer $accessToken"), "Authorization", ApiKeyLocations.HEADER)

    def withApplicationJsonUtf8(): ApiRequest[A] =
      req.copy[A](contentType = "application/json; charset=utf-8")
  }

}

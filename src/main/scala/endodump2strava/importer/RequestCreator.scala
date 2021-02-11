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
      name = o(name.replace(' ', '_')),
      dataType = o(dataType),
      externalId = o(name))
      .addApiKey()
  }

  def getUpload(uploadId: Long): ApiRequest[Upload] =
    UploadsApi.getUploadById(uploadId).addApiKey()

  def createActivity(workout: Workout): ApiRequest[DetailedActivity] = {
    val activity: ActivityParams = workout2Activity(workout)
    def toInt(b: Boolean) = if (b) 1 else 0

    ActivitiesApi.createActivity(
      name = workout.name.getOrElse(activity.activityType.entryName),
      `type` = activity.activityType.entryName,
      startDateLocal = workout.start_time.get,
      elapsedTime = workout.duration_s.get,
      description = workout.message,
      distance = workout.distance_km,
      trainer = o(activity.trainer).map(toInt),
      commute = o(activity.commute).map(toInt))
      .addApiKey()
  }

  def updateActivity(activityId: Long, workout: Workout): ApiRequest[DetailedActivity] = {
    val activity: ActivityParams = workout2Activity(workout)

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

  private def workout2Activity(workout: Workout) = {
    val sport = Sport.withName(workout.sport)
    val activity = Sport2Activity(sport)
    activity
  }

  implicit class RichApiRequest[A](req: ApiRequest[A]) {
    def addApiKey(): ApiRequest[A] =
      req.withApiKey(ApiKeyValue(s"Bearer $accessToken"), "Authorization", ApiKeyLocations.HEADER)

    def withApplicationJsonUtf8(): ApiRequest[A] =
      req.copy[A](contentType = "application/json; charset=utf-8")
  }

}

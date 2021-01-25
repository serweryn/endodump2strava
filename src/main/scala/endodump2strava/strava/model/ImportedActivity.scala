package endodump2strava.strava.model

import java.time.ZonedDateTime

final case class ImportedActivity(
  workoutBasename: String,
  uploadId: Option[Long],
  activityId: Option[Long]
)

final case class ImportedActivityStep(
  workoutBasename: String,
  stepName: String,
  responseCode: Int,
  responseBody: String,
  received: ZonedDateTime
)

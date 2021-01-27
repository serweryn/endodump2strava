package endodump2strava.db

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
  responseHeaders: String,
  received: ZonedDateTime
)

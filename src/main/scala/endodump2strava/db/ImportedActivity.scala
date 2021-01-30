package endodump2strava.db

import java.time.ZonedDateTime

final case class ImportedActivity(
  workoutBasename: String,
  uploadId: Long,
  activityId: Option[Long]
)


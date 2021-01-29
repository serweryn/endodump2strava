package endodump2strava.db

import java.time.{LocalDateTime, ZonedDateTime}

final case class ImportedActivityStep(
  workoutBasename: String,
  stepName: String,
  responseCode: Int,
  responseBody: String,
  responseHeaders: String,
  received: LocalDateTime
)

object ImportedActivityStep {
  val createUpload = "createUpload"
  val getUpload = "getUpload"
  val updateActivity = "updateActivity"
}

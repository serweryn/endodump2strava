package endodump2strava.importer

object ErrorCodes {

  val InvalidApiConnection = 499
  val OtherApiError = 498
  val EmptyFile = 497

  // these are "successful" codes, used to mark activities which shouldn't be processed further
  val DuplicateActivity = 297
  val SuccessfulCodes = Seq(DuplicateActivity)

}

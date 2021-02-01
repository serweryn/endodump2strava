package endodump2strava.importer

object ErrorCodes {

  val InvalidApiConnection = 499
  val OtherApiError = 498

  // these are "successful" codes, used to mark activities which shouldn't be processed further
  val DuplicateActivity = 297
  val EmptyFile = 298
  val SuccessfulCodes = Seq(DuplicateActivity, EmptyFile)

}

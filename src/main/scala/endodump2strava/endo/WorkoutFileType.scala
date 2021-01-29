package endodump2strava.endo

import enumeratum._

import scala.collection.immutable

sealed abstract class WorkoutFileType(val extension: String) extends EnumEntry

object WorkoutFileType extends Enum[WorkoutFileType] {
  lazy val values: immutable.IndexedSeq[WorkoutFileType] = findValues

  case object TcxGz extends WorkoutFileType("tcx.gz")
  case object Json  extends WorkoutFileType("json")

}

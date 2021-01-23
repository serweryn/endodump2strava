package endodump2strava.endo

import enumeratum._

import scala.collection.immutable

sealed trait Sport extends EnumEntry

object Sport extends Enum[Sport] {
  lazy val values: immutable.IndexedSeq[Sport] = findValues

  case object CROSSFIT extends Sport
  case object CYCLING_SPORT extends Sport
  case object CYCLING_TRANSPORTATION extends Sport
  case object FITNESS_WALKING extends Sport
  case object HIKING extends Sport
  case object ICE_SKATING extends Sport
  case object KAYAKING extends Sport
  case object KICK_SCOOTER extends Sport
  case object ORIENTEERING extends Sport
  case object OTHER extends Sport
  case object PILATES extends Sport
  case object RIDING extends Sport
  case object ROLLER_SKATING extends Sport
  case object RUNNING extends Sport
  case object SAILING extends Sport
  case object SKIING_DOWNHILL extends Sport
  case object SPINNING extends Sport
  case object STRETCHING extends Sport
  case object SWIMMING extends Sport
  case object TREADMILL_RUNNING extends Sport
  case object WALKING extends Sport
  case object WEIGHT_TRAINING extends Sport
  case object YOGA extends Sport
}

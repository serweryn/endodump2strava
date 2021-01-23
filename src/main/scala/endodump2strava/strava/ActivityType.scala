package endodump2strava.strava

import enumeratum._

import scala.collection.immutable

sealed trait ActivityType extends EnumEntry

object ActivityType extends Enum[ActivityType] {
  lazy val values: immutable.IndexedSeq[ActivityType] = findValues

  case object AlpineSki extends ActivityType
  case object BackcountrySki extends ActivityType
  case object Canoeing extends ActivityType
  case object Crossfit extends ActivityType
  case object EBikeRide extends ActivityType
  case object Elliptical extends ActivityType
  case object Golf extends ActivityType
  case object Handcycle extends ActivityType
  case object Hike extends ActivityType
  case object IceSkate extends ActivityType
  case object InlineSkate extends ActivityType
  case object Kayaking extends ActivityType
  case object Kitesurf extends ActivityType
  case object NordicSki extends ActivityType
  case object Ride extends ActivityType
  case object RockClimbing extends ActivityType
  case object RollerSki extends ActivityType
  case object Rowing extends ActivityType
  case object Run extends ActivityType
  case object Sail extends ActivityType
  case object Skateboard extends ActivityType
  case object Snowboard extends ActivityType
  case object Snowshoe extends ActivityType
  case object Soccer extends ActivityType
  case object StairStepper extends ActivityType
  case object StandUpPaddling extends ActivityType
  case object Surfing extends ActivityType
  case object Swim extends ActivityType
  case object Velomobile extends ActivityType
  case object VirtualRide extends ActivityType
  case object VirtualRun extends ActivityType
  case object Walk extends ActivityType
  case object WeightTraining extends ActivityType
  case object Wheelchair extends ActivityType
  case object Windsurf extends ActivityType
  case object Workout extends ActivityType
  case object Yoga extends ActivityType
}

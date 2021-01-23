package endodump2strava.importer

import endodump2strava.endo.Sport
import endodump2strava.endo.Sport._
import endodump2strava.strava.ActivityType
import endodump2strava.strava.ActivityType._

object Sport2Activity {
  def apply(sport: Sport): ActivityParams = mapping(sport)

  private val mapping: Map[Sport, ActivityParams] = Map(
    CROSSFIT               -> ActivityParams(Crossfit, false),
    CYCLING_SPORT          -> ActivityParams(Ride, false),
    CYCLING_TRANSPORTATION -> ActivityParams(Ride, true),
    FITNESS_WALKING        -> ActivityParams(Walk, false),
    HIKING                 -> ActivityParams(Hike, false),
    ICE_SKATING            -> ActivityParams(IceSkate, false),
    KAYAKING               -> ActivityParams(Kayaking, false),
    KICK_SCOOTER           -> ActivityParams(Skateboard, false),
    ORIENTEERING           -> ActivityParams(Run, false),
    OTHER                  -> ActivityParams(Workout, false),
    PILATES                -> ActivityParams(Yoga, false),
    RIDING                 -> ActivityParams(Workout, false),
    ROLLER_SKATING         -> ActivityParams(InlineSkate, false),
    RUNNING                -> ActivityParams(Run, false),
    SAILING                -> ActivityParams(Sail, false),
    SKIING_DOWNHILL        -> ActivityParams(AlpineSki, false),
    SPINNING               -> ActivityParams(Ride, false, true),
    STRETCHING             -> ActivityParams(Yoga, false),
    SWIMMING               -> ActivityParams(Swim, false),
    TREADMILL_RUNNING      -> ActivityParams(Run, false, true),
    WALKING                -> ActivityParams(Walk, false),
    WEIGHT_TRAINING        -> ActivityParams(WeightTraining, false),
    YOGA                   -> ActivityParams(Yoga, false)
  )
}

final case class ActivityParams(
  activityType: ActivityType,
  commute: Boolean,
  trainer: Boolean = false
)

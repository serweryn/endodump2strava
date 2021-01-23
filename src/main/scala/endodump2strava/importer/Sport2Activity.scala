package endodump2strava.importer

import endodump2strava.endo.Sport
import endodump2strava.endo.Sport._
import endodump2strava.strava.ActivityType
import endodump2strava.strava.ActivityType._

object Sport2Activity {
  def apply(sport: Sport): ActivityParams = mapping(sport)

  private val mapping: Map[Sport, ActivityParams] = Map(
    CROSSFIT               -> ActivityParams(Crossfit),
    CYCLING_SPORT          -> ActivityParams(Ride),
    CYCLING_TRANSPORTATION -> ActivityParams(Ride, commute = true),
    FITNESS_WALKING        -> ActivityParams(Walk),
    HIKING                 -> ActivityParams(Hike),
    ICE_SKATING            -> ActivityParams(IceSkate),
    KAYAKING               -> ActivityParams(Kayaking),
    KICK_SCOOTER           -> ActivityParams(Skateboard),
    ORIENTEERING           -> ActivityParams(Run),
    OTHER                  -> ActivityParams(Workout),
    PILATES                -> ActivityParams(Yoga),
    RIDING                 -> ActivityParams(Workout),
    ROLLER_SKATING         -> ActivityParams(InlineSkate),
    RUNNING                -> ActivityParams(Run),
    SAILING                -> ActivityParams(Sail),
    SKIING_DOWNHILL        -> ActivityParams(AlpineSki),
    SPINNING               -> ActivityParams(Ride, trainer = true),
    STRETCHING             -> ActivityParams(Yoga),
    SWIMMING               -> ActivityParams(Swim),
    TREADMILL_RUNNING      -> ActivityParams(Run, trainer = true),
    WALKING                -> ActivityParams(Walk),
    WEIGHT_TRAINING        -> ActivityParams(WeightTraining),
    YOGA                   -> ActivityParams(Yoga)
  )
}

final case class ActivityParams(
  activityType: ActivityType,
  commute: Boolean = false,
  trainer: Boolean = false
)

/**
 * Strava API v3
 * The [Swagger Playground](https://developers.strava.com/playground) is the easiest way to familiarize yourself with the Strava API by submitting HTTP requests and observing the responses before you write any client code. It will show what a response will look like with different endpoints depending on the authorization scope you receive from your athletes. To use the Playground, go to https://www.strava.com/settings/api and change your “Authorization Callback Domain” to developers.strava.com. Please note, we only support Swagger 2.0. There is a known issue where you can only select one scope at a time. For more information, please check the section “client code” at https://developers.strava.com/docs.
 *
 * OpenAPI spec version: 3.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package endodump2strava.strava.api

import endodump2strava.strava.model._
import org.json4s._
import scala.reflect.ClassTag

object EnumsSerializers {

  def all: Seq[Serializer[_]] = Seq[Serializer[_]]() :+
    new EnumNameSerializer(ActivityZoneEnums.`Type`) :+
    new EnumNameSerializer(AltitudeStreamEnums.Resolution) :+
    new EnumNameSerializer(AltitudeStreamEnums.SeriesType) :+
    new EnumNameSerializer(BaseStreamEnums.Resolution) :+
    new EnumNameSerializer(BaseStreamEnums.SeriesType) :+
    new EnumNameSerializer(CadenceStreamEnums.Resolution) :+
    new EnumNameSerializer(CadenceStreamEnums.SeriesType) :+
    new EnumNameSerializer(DetailedAthleteEnums.Sex) :+
    new EnumNameSerializer(DetailedAthleteEnums.MeasurementPreference) :+
    new EnumNameSerializer(DetailedClubEnums.SportType) :+
    new EnumNameSerializer(DetailedClubEnums.Membership) :+
    new EnumNameSerializer(DetailedSegmentEnums.ActivityType) :+
    new EnumNameSerializer(DistanceStreamEnums.Resolution) :+
    new EnumNameSerializer(DistanceStreamEnums.SeriesType) :+
    new EnumNameSerializer(ExplorerSegmentEnums.ClimbCategoryDesc) :+
    new EnumNameSerializer(HeartrateStreamEnums.Resolution) :+
    new EnumNameSerializer(HeartrateStreamEnums.SeriesType) :+
    new EnumNameSerializer(LatLngStreamEnums.Resolution) :+
    new EnumNameSerializer(LatLngStreamEnums.SeriesType) :+
    new EnumNameSerializer(MovingStreamEnums.Resolution) :+
    new EnumNameSerializer(MovingStreamEnums.SeriesType) :+
    new EnumNameSerializer(PowerStreamEnums.Resolution) :+
    new EnumNameSerializer(PowerStreamEnums.SeriesType) :+
    new EnumNameSerializer(RunningRaceEnums.MeasurementPreference) :+
    new EnumNameSerializer(SmoothGradeStreamEnums.Resolution) :+
    new EnumNameSerializer(SmoothGradeStreamEnums.SeriesType) :+
    new EnumNameSerializer(SmoothVelocityStreamEnums.Resolution) :+
    new EnumNameSerializer(SmoothVelocityStreamEnums.SeriesType) :+
    new EnumNameSerializer(SummaryAthleteEnums.Sex) :+
    new EnumNameSerializer(SummaryClubEnums.SportType) :+
    new EnumNameSerializer(SummarySegmentEnums.ActivityType) :+
    new EnumNameSerializer(TemperatureStreamEnums.Resolution) :+
    new EnumNameSerializer(TemperatureStreamEnums.SeriesType) :+
    new EnumNameSerializer(TimeStreamEnums.Resolution) :+
    new EnumNameSerializer(TimeStreamEnums.SeriesType)

  private class EnumNameSerializer[E <: Enumeration: ClassTag](enum: E)
    extends Serializer[E#Value] {
    import JsonDSL._

    val EnumerationClass: Class[E#Value] = classOf[E#Value]

    def deserialize(implicit format: Formats):
    PartialFunction[(TypeInfo, JValue), E#Value] = {
      case (t @ TypeInfo(EnumerationClass, _), json) if isValid(json) =>
        json match {
          case JString(value) =>
            enum.withName(value)
          case value =>
            throw new MappingException(s"Can't convert $value to $EnumerationClass")
        }
    }

    private[this] def isValid(json: JValue) = json match {
      case JString(value) if enum.values.exists(_.toString == value) => true
      case _ => false
    }

    def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
      case i: E#Value => i.toString
    }
  }

}
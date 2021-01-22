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
package endodump2strava.strava.model

import io.swagger.client.core.ApiModel
import org.joda.time.DateTime
import java.util.UUID

case class DetailedSegment (
  /* The unique identifier of this segment */
  id: Option[Long] = None,
  /* The name of this segment */
  name: Option[String] = None,
  activityType: Option[DetailedSegmentEnums.ActivityType] = None,
  /* The segment's distance, in meters */
  distance: Option[Float] = None,
  /* The segment's average grade, in percents */
  averageGrade: Option[Float] = None,
  /* The segments's maximum grade, in percents */
  maximumGrade: Option[Float] = None,
  /* The segments's highest elevation, in meters */
  elevationHigh: Option[Float] = None,
  /* The segments's lowest elevation, in meters */
  elevationLow: Option[Float] = None,
  startLatlng: Option[LatLng] = None,
  endLatlng: Option[LatLng] = None,
  /* The category of the climb [0, 5]. Higher is harder ie. 5 is Hors catégorie, 0 is uncategorized in climb_category. */
  climbCategory: Option[Int] = None,
  /* The segments's city. */
  city: Option[String] = None,
  /* The segments's state or geographical region. */
  state: Option[String] = None,
  /* The segment's country. */
  country: Option[String] = None,
  /* Whether this segment is private. */
  `private`: Option[Boolean] = None,
  athletePrEffort: Option[SummarySegmentEffort] = None,
  athleteSegmentStats: Option[SummaryPRSegmentEffort] = None,
  /* The time at which the segment was created. */
  createdAt: Option[DateTime] = None,
  /* The time at which the segment was last updated. */
  updatedAt: Option[DateTime] = None,
  /* The segment's total elevation gain. */
  totalElevationGain: Option[Float] = None,
  map: Option[PolylineMap] = None,
  /* The total number of efforts for this segment */
  effortCount: Option[Int] = None,
  /* The number of unique athletes who have an effort for this segment */
  athleteCount: Option[Int] = None,
  /* Whether this segment is considered hazardous */
  hazardous: Option[Boolean] = None,
  /* The number of stars for this segment */
  starCount: Option[Int] = None
) extends ApiModel

object DetailedSegmentEnums {

  type ActivityType = ActivityType.Value
  object ActivityType extends Enumeration {
    val Ride = Value("Ride")
    val Run = Value("Run")
  }

}


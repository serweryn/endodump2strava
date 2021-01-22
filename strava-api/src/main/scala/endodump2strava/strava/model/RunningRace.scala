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

case class RunningRace (
  /* The unique identifier of this race. */
  id: Option[Long] = None,
  /* The name of this race. */
  name: Option[String] = None,
  /* The type of this race. */
  runningRaceType: Option[Int] = None,
  /* The race's distance, in meters. */
  distance: Option[Float] = None,
  /* The time at which the race begins started in the local timezone. */
  startDateLocal: Option[DateTime] = None,
  /* The name of the city in which the race is taking place. */
  city: Option[String] = None,
  /* The name of the state or geographical region in which the race is taking place. */
  state: Option[String] = None,
  /* The name of the country in which the race is taking place. */
  country: Option[String] = None,
  /* The set of routes that cover this race's course. */
  routeIds: Option[Seq[Long]] = None,
  /* The unit system in which the race should be displayed. */
  measurementPreference: Option[RunningRaceEnums.MeasurementPreference] = None,
  /* The vanity URL of this race on Strava. */
  url: Option[String] = None,
  /* The URL of this race's website. */
  websiteUrl: Option[String] = None
) extends ApiModel

object RunningRaceEnums {

  type MeasurementPreference = MeasurementPreference.Value
  object MeasurementPreference extends Enumeration {
    val Feet = Value("feet")
    val Meters = Value("meters")
  }

}


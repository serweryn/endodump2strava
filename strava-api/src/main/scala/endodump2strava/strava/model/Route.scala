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

case class Route (
  athlete: Option[SummaryAthlete] = None,
  /* The description of the route */
  description: Option[String] = None,
  /* The route's distance, in meters */
  distance: Option[Float] = None,
  /* The route's elevation gain. */
  elevationGain: Option[Float] = None,
  /* The unique identifier of this route */
  id: Option[Long] = None,
  /* The unique identifier of the route in string format */
  idStr: Option[String] = None,
  map: Option[PolylineMap] = None,
  /* The name of this route */
  name: Option[String] = None,
  /* Whether this route is private */
  `private`: Option[Boolean] = None,
  /* Whether this route is starred by the logged-in athlete */
  starred: Option[Boolean] = None,
  /* An epoch timestamp of when the route was created */
  timestamp: Option[Int] = None,
  /* This route's type (1 for ride, 2 for runs) */
  `type`: Option[Int] = None,
  /* This route's sub-type (1 for road, 2 for mountain bike, 3 for cross, 4 for trail, 5 for mixed) */
  subType: Option[Int] = None,
  /* The time at which the route was created */
  createdAt: Option[DateTime] = None,
  /* The time at which the route was last updated */
  updatedAt: Option[DateTime] = None,
  /* Estimated time in seconds for the authenticated athlete to complete route */
  estimatedMovingTime: Option[Int] = None,
  /* The segments traversed by this route */
  segments: Option[Seq[SummarySegment]] = None
) extends ApiModel



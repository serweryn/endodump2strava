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

import endodump2strava.strava.model.ActivityZone
import endodump2strava.strava.model.Comment
import org.joda.time.DateTime
import endodump2strava.strava.model.DetailedActivity
import endodump2strava.strava.model.Fault
import endodump2strava.strava.model.Lap
import endodump2strava.strava.model.SummaryActivity
import endodump2strava.strava.model.SummaryAthlete
import endodump2strava.strava.model.UpdatableActivity
import io.swagger.client.core._
import io.swagger.client.core.CollectionFormats._
import io.swagger.client.core.ApiKeyLocations._

object ActivitiesApi {

  /**
   * Creates a manual activity for an athlete, requires activity:write scope.
   * 
   * Expected answers:
   *   code 201 : DetailedActivity (The activity&#39;s detailed representation.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param name The name of the activity.
   * @param `type` Type of activity. For example - Run, Ride etc.
   * @param startDateLocal ISO 8601 formatted date time.
   * @param elapsedTime In seconds.
   * @param description Description of the activity.
   * @param distance In meters.
   * @param trainer Set to 1 to mark as a trainer activity.
   * @param commute Set to 1 to mark as commute.
   */
  def createActivity(name: String, `type`: String, startDateLocal: DateTime, elapsedTime: Int, description: Option[String] = None, distance: Option[Float] = None, trainer: Option[Int] = None, commute: Option[Int] = None): ApiRequest[DetailedActivity] =
    ApiRequest[DetailedActivity](ApiMethods.POST, "https://www.strava.com/api/v3", "/activities", "application/json")
      .withFormParam("name", name)
      .withFormParam("type", `type`)
      .withFormParam("start_date_local", startDateLocal)
      .withFormParam("elapsed_time", elapsedTime)
      .withFormParam("description", description)
      .withFormParam("distance", distance)
      .withFormParam("trainer", trainer)
      .withFormParam("commute", commute)
      .withSuccessResponse[DetailedActivity](201)
      .withDefaultErrorResponse[Fault]
        /**
   * Returns the given activity that is owned by the authenticated athlete. Requires activity:read for Everyone and Followers activities. Requires activity:read_all for Only Me activities.
   * 
   * Expected answers:
   *   code 200 : DetailedActivity (The activity&#39;s detailed representation.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param id The identifier of the activity.
   * @param includeAllEfforts To include all segments efforts.
   */
  def getActivityById(id: Long, includeAllEfforts: Option[Boolean] = None): ApiRequest[DetailedActivity] =
    ApiRequest[DetailedActivity](ApiMethods.GET, "https://www.strava.com/api/v3", "/activities/{id}", "application/json")
      .withQueryParam("include_all_efforts", includeAllEfforts)
      .withPathParam("id", id)
      .withSuccessResponse[DetailedActivity](200)
      .withDefaultErrorResponse[Fault]
        /**
   * Returns the comments on the given activity. Requires activity:read for Everyone and Followers activities. Requires activity:read_all for Only Me activities.
   * 
   * Expected answers:
   *   code 200 : Seq[Comment] (Comments.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param id The identifier of the activity.
   * @param page Page number. Defaults to 1.
   * @param perPage Number of items per page. Defaults to 30.
   */
  def getCommentsByActivityId(id: Long, page: Option[Int] = None, perPage: Option[Int]): ApiRequest[Seq[Comment]] =
    ApiRequest[Seq[Comment]](ApiMethods.GET, "https://www.strava.com/api/v3", "/activities/{id}/comments", "application/json")
      .withQueryParam("page", page)
      .withQueryParam("per_page", perPage)
      .withPathParam("id", id)
      .withSuccessResponse[Seq[Comment]](200)
      .withDefaultErrorResponse[Fault]
        /**
   * Returns the athletes who kudoed an activity identified by an identifier. Requires activity:read for Everyone and Followers activities. Requires activity:read_all for Only Me activities.
   * 
   * Expected answers:
   *   code 200 : Seq[SummaryAthlete] (Comments.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param id The identifier of the activity.
   * @param page Page number. Defaults to 1.
   * @param perPage Number of items per page. Defaults to 30.
   */
  def getKudoersByActivityId(id: Long, page: Option[Int] = None, perPage: Option[Int]): ApiRequest[Seq[SummaryAthlete]] =
    ApiRequest[Seq[SummaryAthlete]](ApiMethods.GET, "https://www.strava.com/api/v3", "/activities/{id}/kudos", "application/json")
      .withQueryParam("page", page)
      .withQueryParam("per_page", perPage)
      .withPathParam("id", id)
      .withSuccessResponse[Seq[SummaryAthlete]](200)
      .withDefaultErrorResponse[Fault]
        /**
   * Returns the laps of an activity identified by an identifier. Requires activity:read for Everyone and Followers activities. Requires activity:read_all for Only Me activities.
   * 
   * Expected answers:
   *   code 200 : Seq[Lap] (Activity Laps.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param id The identifier of the activity.
   */
  def getLapsByActivityId(id: Long): ApiRequest[Seq[Lap]] =
    ApiRequest[Seq[Lap]](ApiMethods.GET, "https://www.strava.com/api/v3", "/activities/{id}/laps", "application/json")
      .withPathParam("id", id)
      .withSuccessResponse[Seq[Lap]](200)
      .withDefaultErrorResponse[Fault]
        /**
   * Returns the activities of an athlete for a specific identifier. Requires activity:read. Only Me activities will be filtered out unless requested by a token with activity:read_all.
   * 
   * Expected answers:
   *   code 200 : Seq[SummaryActivity] (The authenticated athlete&#39;s activities)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param before An epoch timestamp to use for filtering activities that have taken place before a certain time.
   * @param after An epoch timestamp to use for filtering activities that have taken place after a certain time.
   * @param page Page number. Defaults to 1.
   * @param perPage Number of items per page. Defaults to 30.
   */
  def getLoggedInAthleteActivities(before: Option[Int] = None, after: Option[Int] = None, page: Option[Int] = None, perPage: Option[Int]): ApiRequest[Seq[SummaryActivity]] =
    ApiRequest[Seq[SummaryActivity]](ApiMethods.GET, "https://www.strava.com/api/v3", "/athlete/activities", "application/json")
      .withQueryParam("before", before)
      .withQueryParam("after", after)
      .withQueryParam("page", page)
      .withQueryParam("per_page", perPage)
      .withSuccessResponse[Seq[SummaryActivity]](200)
      .withDefaultErrorResponse[Fault]
        /**
   * Summit Feature. Returns the zones of a given activity. Requires activity:read for Everyone and Followers activities. Requires activity:read_all for Only Me activities.
   * 
   * Expected answers:
   *   code 200 : Seq[ActivityZone] (Activity Zones.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param id The identifier of the activity.
   */
  def getZonesByActivityId(id: Long): ApiRequest[Seq[ActivityZone]] =
    ApiRequest[Seq[ActivityZone]](ApiMethods.GET, "https://www.strava.com/api/v3", "/activities/{id}/zones", "application/json")
      .withPathParam("id", id)
      .withSuccessResponse[Seq[ActivityZone]](200)
      .withDefaultErrorResponse[Fault]
        /**
   * Updates the given activity that is owned by the authenticated athlete. Requires activity:write. Also requires activity:read_all in order to update Only Me activities
   * 
   * Expected answers:
   *   code 200 : DetailedActivity (The activity&#39;s detailed representation.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param id The identifier of the activity.
   * @param body 
   */
  def updateActivityById(id: Long, body: Option[UpdatableActivity] = None): ApiRequest[DetailedActivity] =
    ApiRequest[DetailedActivity](ApiMethods.PUT, "https://www.strava.com/api/v3", "/activities/{id}", "application/json")
      .withBody(body)
      .withPathParam("id", id)
      .withSuccessResponse[DetailedActivity](200)
      .withDefaultErrorResponse[Fault]
      

}


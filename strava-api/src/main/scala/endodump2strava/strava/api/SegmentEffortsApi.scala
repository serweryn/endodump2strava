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

import org.joda.time.DateTime
import endodump2strava.strava.model.DetailedSegmentEffort
import endodump2strava.strava.model.Fault
import io.swagger.client.core._
import io.swagger.client.core.CollectionFormats._
import io.swagger.client.core.ApiKeyLocations._

object SegmentEffortsApi {

  /**
   * Returns a set of the authenticated athlete&#39;s segment efforts for a given segment.  Requires subscription.
   * 
   * Expected answers:
   *   code 200 : Seq[DetailedSegmentEffort] (List of segment efforts.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param segmentId The identifier of the segment.
   * @param startDateLocal ISO 8601 formatted date time.
   * @param endDateLocal ISO 8601 formatted date time.
   * @param perPage Number of items per page. Defaults to 30.
   */
  def getEffortsBySegmentId(segmentId: Int, startDateLocal: Option[DateTime] = None, endDateLocal: Option[DateTime] = None, perPage: Option[Int]): ApiRequest[Seq[DetailedSegmentEffort]] =
    ApiRequest[Seq[DetailedSegmentEffort]](ApiMethods.GET, "https://www.strava.com/api/v3", "/segment_efforts", "application/json")
      .withQueryParam("segment_id", segmentId)
      .withQueryParam("start_date_local", startDateLocal)
      .withQueryParam("end_date_local", endDateLocal)
      .withQueryParam("per_page", perPage)
      .withSuccessResponse[Seq[DetailedSegmentEffort]](200)
      .withDefaultErrorResponse[Fault]
        /**
   * Returns a segment effort from an activity that is owned by the authenticated athlete. Requires subscription.
   * 
   * Expected answers:
   *   code 200 : DetailedSegmentEffort (Representation of a segment effort.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param id The identifier of the segment effort.
   */
  def getSegmentEffortById(id: Long): ApiRequest[DetailedSegmentEffort] =
    ApiRequest[DetailedSegmentEffort](ApiMethods.GET, "https://www.strava.com/api/v3", "/segment_efforts/{id}", "application/json")
      .withPathParam("id", id)
      .withSuccessResponse[DetailedSegmentEffort](200)
      .withDefaultErrorResponse[Fault]
      

}

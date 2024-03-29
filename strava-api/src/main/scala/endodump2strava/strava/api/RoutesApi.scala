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

import endodump2strava.strava.model.Fault
import endodump2strava.strava.model.Route
import io.swagger.client.core._
import io.swagger.client.core.CollectionFormats._
import io.swagger.client.core.ApiKeyLocations._

object RoutesApi {

  /**
   * Returns a GPX file of the route. Requires read_all scope for private routes.
   * 
   * Expected answers:
   *   code 200 :  (A GPX file with the route.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param id The identifier of the route.
   */
  def getRouteAsGPX(id: Long): ApiRequest[Unit] =
    ApiRequest[Unit](ApiMethods.GET, "https://www.strava.com/api/v3", "/routes/{id}/export_gpx", "application/json")
      .withPathParam("id", id)
      .withSuccessResponse[Unit](200)
      .withDefaultErrorResponse[Fault]
        /**
   * Returns a TCX file of the route. Requires read_all scope for private routes.
   * 
   * Expected answers:
   *   code 200 :  (A TCX file with the route.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param id The identifier of the route.
   */
  def getRouteAsTCX(id: Long): ApiRequest[Unit] =
    ApiRequest[Unit](ApiMethods.GET, "https://www.strava.com/api/v3", "/routes/{id}/export_tcx", "application/json")
      .withPathParam("id", id)
      .withSuccessResponse[Unit](200)
      .withDefaultErrorResponse[Fault]
        /**
   * Returns a route using its identifier. Requires read_all scope for private routes.
   * 
   * Expected answers:
   *   code 200 : Route (A representation of the route.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param id The identifier of the route.
   */
  def getRouteById(id: Long): ApiRequest[Route] =
    ApiRequest[Route](ApiMethods.GET, "https://www.strava.com/api/v3", "/routes/{id}", "application/json")
      .withPathParam("id", id)
      .withSuccessResponse[Route](200)
      .withDefaultErrorResponse[Fault]
        /**
   * Returns a list of the routes created by the authenticated athlete. Private routes are filtered out unless requested by a token with read_all scope.
   * 
   * Expected answers:
   *   code 200 : Seq[Route] (A representation of the route.)
   *   code 0 : Fault (Unexpected error.)
   * 
   * @param page Page number. Defaults to 1.
   * @param perPage Number of items per page. Defaults to 30.
   */
  def getRoutesByAthleteId(page: Option[Int] = None, perPage: Option[Int]): ApiRequest[Seq[Route]] =
    ApiRequest[Seq[Route]](ApiMethods.GET, "https://www.strava.com/api/v3", "/athletes/{id}/routes", "application/json")
      .withQueryParam("page", page)
      .withQueryParam("per_page", perPage)
      .withSuccessResponse[Seq[Route]](200)
      .withDefaultErrorResponse[Fault]
      

}


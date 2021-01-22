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

case class Upload (
  /* The unique identifier of the upload */
  id: Option[Long] = None,
  /* The unique identifier of the upload in string format */
  idStr: Option[String] = None,
  /* The external identifier of the upload */
  externalId: Option[String] = None,
  /* The error associated with this upload */
  error: Option[String] = None,
  /* The status of this upload */
  status: Option[String] = None,
  /* The identifier of the activity this upload resulted into */
  activityId: Option[Long] = None
) extends ApiModel


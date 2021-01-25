package endodump2strava.strava.api

import endodump2strava.strava.model.{ApiTokenInfo, Fault}
import io.swagger.client.core.{ApiMethods, ApiRequest}

object OAuthApi {

  def refreshToken(clientId: String, clientSecret: String, refreshToken: String): ApiRequest[ApiTokenInfo] =
    ApiRequest[ApiTokenInfo](
      ApiMethods.POST,
      "https://www.strava.com/api/v3",
      "/oauth/token",
      "application/x-www-form-urlencoded")
      .withQueryParam("client_id", clientId)
      .withQueryParam("client_secret", clientSecret)
      .withQueryParam("grant_type", "refresh_token")
      .withQueryParam("refresh_token", refreshToken)
      .withSuccessResponse[ApiTokenInfo](200)
      .withDefaultErrorResponse[Fault]

}

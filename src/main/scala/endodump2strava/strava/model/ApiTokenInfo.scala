package endodump2strava.strava.model

import io.swagger.client.core.ApiModel

case class ApiTokenInfo(
  tokenType: Option[String] = None,
  accessToken: Option[String] = None,
  expiresAt: Option[Int] = None,
  expiresIn: Option[Int] = None,
  refreshToken: Option[String] = None
) extends ApiModel

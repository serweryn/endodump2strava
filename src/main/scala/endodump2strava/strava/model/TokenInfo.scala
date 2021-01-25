package endodump2strava.strava.model

final case class TokenInfo(
  user: String,
  tokenType: String,
  accessToken: String,
  expiresAt: Int,
  refreshToken: String
)

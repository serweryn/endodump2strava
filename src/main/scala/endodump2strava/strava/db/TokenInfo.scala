package endodump2strava.strava.db

final case class TokenInfo(
  user: String,
  tokenType: String,
  accessToken: String,
  expiresAt: Int,
  refreshToken: String
)

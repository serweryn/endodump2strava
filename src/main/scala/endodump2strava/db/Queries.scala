package endodump2strava.db

import io.getquill.{H2JdbcContext, SnakeCase}

class Queries(val sqlCtx: H2JdbcContext[SnakeCase]) {

  import sqlCtx._

  def selectRefreshToken(user: String): List[TokenInfo] =
    sqlCtx.run {
      quote {
        query[TokenInfo].filter(_.user == lift(user))
      }
    }

  def deleteTokenInfo(user: String): Long =
    sqlCtx.run {
      quote {
        query[TokenInfo].filter(_.user == lift(user)).delete
      }
    }

  def insertTokenInfo(user: String, tokenType: String, accessToken: String, expiresAt: Int, refreshToken: String): Long =
    sqlCtx.run {
      quote {
        query[TokenInfo].insert(TokenInfo(lift(user), lift(tokenType), lift(accessToken), lift(expiresAt), lift(refreshToken)))
      }
    }

}

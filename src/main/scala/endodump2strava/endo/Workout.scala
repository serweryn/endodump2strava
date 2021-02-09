package endodump2strava.endo

import org.joda.time.DateTime
import play.api.libs.json.{JsObject, Json, JsValue, OFormat, Reads}

final case class Workout(
  sport: String,
  name: Option[String],
  message: Option[String],
  start_time: Option[DateTime],
  duration_s: Option[Int],
  distance_km: Option[Float]
)

object Workout {
  def apply(json: JsValue): Workout = {
    val ar = json.as[Array[JsObject]]
    def get[A](name: String)(implicit r: Reads[A]) = ar.find(_.keys.contains(name)).map(_.value(name).as[A])
    def gets(name: String) = get[String](name)
    def geti(name: String) = get[Int](name)
    def getf(name: String) = get[Float](name)
    Workout(
      gets("sport").get,
      gets("name"),
      gets("message").orElse(gets("notes")),
      gets("start_time").map(_.replace(' ', 'T')).map(DateTime.parse),
      getf("duration_s").map(_.toInt),
      getf("distance_km")
    )
  }
}

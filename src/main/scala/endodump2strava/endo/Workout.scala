package endodump2strava.endo

import play.api.libs.json.{JsObject, JsValue, Json, OFormat}

final case class Workout(
  sport: String,
  name: Option[String],
  message: Option[String]
)

object Workout {
  def apply(json: JsValue): Workout = {
    val ar = json.as[Array[JsObject]]
    def get(name: String) = ar.find(_.keys.contains(name)).map(_.value(name).as[String])
    Workout(get("sport").get, get("name"), get("message"))
  }

  implicit val format: OFormat[Workout] = Json.format[Workout]
}

package endodump2strava.importer

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object Main extends App {

  private implicit val system: ActorSystem = ActorSystem()
  private val importer = new Importer()

  val importTask = system.scheduler.schedule(0 seconds, 15 minutes)(importer.doImport())
  system.registerOnTermination(importTask.cancel())

}

package endodump2strava.importer

import akka.actor.ActorSystem

import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext
import scala.language.postfixOps

object Main extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = system.dispatcher
  val importer = new Importer()

  val importTask = system.scheduler.schedule(0 seconds, 15 minutes)(importer.doImport())
  system.registerOnTermination(importTask.cancel())

}

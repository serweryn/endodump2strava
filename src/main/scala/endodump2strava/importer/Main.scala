package endodump2strava.importer

import akka.actor.ActorSystem

import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext
import scala.language.postfixOps

object Main extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = system.dispatcher

  def doImport(): Unit = {
    val importer = new Importer()
    importer.doImport()
  }

  val importTask = system.scheduler.schedule(0 seconds, 15 minutes)(doImport())
  system.registerOnTermination(importTask.cancel())

}

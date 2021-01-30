package endodump2strava.importer

import akka.actor.ActorSystem

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future, Promise}
import scala.concurrent.duration.{Duration, FiniteDuration}

object Implicits {

  implicit class RichFuture[A](future: Future[A])(implicit system: ActorSystem) {
    private implicit val ec: ExecutionContextExecutor = system.dispatcher

    /** Non-blocking sleep and return original future */
    def sleep(d: FiniteDuration): Future[A] = {
      val promise = Promise[Unit]()
      val sleep = system.scheduler.scheduleOnce(d)(promise.success(()))
      promise.future.flatMap(_ => future)
    }
  }

}

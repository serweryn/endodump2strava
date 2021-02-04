package endodump2strava.importer

import io.swagger.client.core.{ApiError, ApiInvoker, ApiRequest, ApiResponse}

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{ExecutionContextExecutor, Future}

class GuardedApiInvoker(invoker: ApiInvoker, maxRequestsInBatch: Int) {

  private implicit val ec: ExecutionContextExecutor = invoker.ec

  private val invalidateOnCodes = Seq(401, 429)

  val invalidCode: Int = ErrorCodes.InvalidApiConnection
  val invalidMsg = "You cannot make any more requests now, please try again in 15 minutes"

  private[this] val requestsLeft = new AtomicInteger(maxRequestsInBatch)

  def execute[T](r: ApiRequest[T]): Future[ApiResponse[T]] = {
    if (requestsLeft.getAndDecrement() <= 0) respondWithInvalid()
    else {
      // execute real request and invalidate this invoker if got specific error response
      val f = invoker.execute(r)
      f recoverWith {
        case ApiError(code, _, _, _, _) =>
          if (invalidateOnCodes.contains(code)) requestsLeft.set(0)
          f
      }
    }
  }

  def valid: Boolean = requestsLeft.get() > 0

  def reset(): Unit = requestsLeft.set(maxRequestsInBatch)

  private def respondWithInvalid[T](): Future[ApiResponse[T]] =
    Future.failed(ApiError(invalidCode, invalidMsg, None))

}

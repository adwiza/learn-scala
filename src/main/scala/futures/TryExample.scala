package futures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object TryExample extends App {

  /* val i = try {
    1/0
  }catch {
    case ex: Throwable => println(1); 1
  }

  lazy val exp = 1/0

  println(Try(exp))
  println(Try(1))

  println(Try(1).toEither)
  Success(1)
  Failure(new Exception("123"))

  Try(1).map(_ + 1)
    .flatMap(x => Success(x + x))
    .recover{
      case x => x
    }
    .recoverWith{
      case x => Try(1)
    }

  println(Future.fromTry(Try(1/0)))
 */
  println(Try(1).filter(_ % 2 == 0)
    .recover {
      case ex =>
        println(ex)
        1
    }
  )

//  Future.successful(1).filter()
  // Try
  // Failure
  // Success
}
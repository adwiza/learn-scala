package futures

import scala.concurrent.Future

object Futures_Example extends App {

  def unit[T](x: T): Some[T] = Some(x)

  val i = 1
  val s: Option[Int] = for {
    x1 <- Option(i)
    x2 <- if (x1 % 2 == 0) Some(x1) else None
    _ = println("Step 3")
    x3 <- Some(x2 + 1)
  } yield x3

  val opt: Option[Int] = Option(1).flatMap(x => Option(x * x))
}

object Monad {
  def unit[T](x: T): Monad[T] = ???
}

trait Monad[T] {
  def flatMap[U](f: T => Monad[U]): Monad[U] // bind
}

trait SocialServiceDummy {
  def getUserInfo(userId: Long): Future[String] = Future.successful("user")
}
trait SocialService {

  type Effect[T] = Monad[T]

  def getUserInfo(userId: Long): Effect[String]
}
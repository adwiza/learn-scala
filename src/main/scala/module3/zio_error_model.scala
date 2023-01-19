package module3

import zio.Console.printLine
import zio.{Clock, Console, IO, RIO, Task, UIO, URIO, ZIO}

import scala.concurrent.Future
import scala.io.StdIn
import scala.util.Try

/**
 * ZIO[-R, +E, +A] ----> R => Ether[E, A]
 */
object toyModel2 {

  /**
   * Используя executable encoding реализуем свой zio
   *
   */
  case class ZIO[-R, +E, +A](run: R => Either[E, A]) {
    self =>
    def foldM[R1 <: R, E1, B](
                               failure: E => ZIO[R1, E1, B],
                               success: A => ZIO[R1, E1, B]
                             ): ZIO[R1, E1, B] = {
      ZIO(r =>
        self
          .run(r)
          .fold(failure, success)
          .run(r))
    }

    def orElse[R1 <: R, E1, A1 >: A](other: ZIO[R1, E1, A1]): ZIO[R1, E1, A1] =
      foldM(
        _ => other,
        v => ZIO(_ => Right(v))
      )

    def option: ZIO[R, Nothing, Option[A]] =
      foldM(
        _ => ZIO(_ => Right(None)),
        v => ZIO(_ => Right(Some(v)))
      )

    def mapError[E1](f: E => E1): ZIO[R, E1, A] = foldM(
      e => ZIO(r => Left(f(e))),
      v => ZIO(r => Right(v))
    )
  }

  sealed trait UserRegistrationError

  case object  InvalidEmail extends UserRegistrationError
  case object WeakPassword extends UserRegistrationError

  lazy val checkEmail: IO[InvalidEmail.type, String] = ???
  lazy val checkPassword: IO[WeakPassword.type, String] = ???

  lazy val userRegistrationCheck: zio.ZIO[Any, UserRegistrationError, (String, String)] =
    checkEmail.zip(checkPassword)

  lazy val io1: IO[String, String] = ???
  lazy val io2: IO[Int, String] = ???

  lazy val io3: zio.ZIO[Any, Either[String, Int], (String, String)] =
    io1.mapError(Left(_)).zip(io2.mapError(Right(_)))


  val _: zio.ZIO[Any, Any, (String, String)] = io1.zip(io2)

  def either: Either[String, Int] = ???

  def errorToErrorCode(str: String): Int = ???

  lazy val effFromEither: IO[String, Int] = zio.ZIO.fromEither(either)

  /**
   * Залогировать ошибку effFromEither, не меняя её тип и тип возвращаемого значения
   */
  lazy val _: zio.ZIO[Console, String, Int] = effFromEither.tapError{str =>
    printLine(str)
  }

  lazy val _: zio.ZIO[Any, Int, Int] = effFromEither.mapError(errorToErrorCode)

  lazy val effEitherOrResult: UIO[Either[String, Int]] = effFromEither.either

  lazy val _: zio.IO[String, Int] = effEitherOrResult.absolve

  // orDie operator
  type User = String
  type UserId = Int

  sealed trait  NotificationError
  case object NotificationErrorByEmailFailed extends NotificationError
  case object  NotificationErrorBySMSFailed extends NotificationError

  def gerUserById(userId: UserId): Task[User] = ???
  def sendEmail(user: User, email: String): IO[NotificationErrorByEmailFailed.type, Unit] = ???
  def sendSMS(user: User, phone: String): IO[NotificationErrorBySMSFailed.type, Unit] = ???
  def sendNotification(userId: UserId): IO[NotificationError, Unit] = for {
    user <- gerUserById(1).orDie
    _ <- sendEmail(user, "email")
    _ <- sendSMS(user, "453453")
  } yield ()


  lazy val _ = ???






}

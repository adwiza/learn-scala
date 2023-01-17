package module3

import zio.{IO, RIO, Task, UIO, URIO, ZIO}

import scala.concurrent.Future
import scala.io.StdIn
import scala.util.Try

/**
 * ZIO[-R, +E, +A] ----> R => Ether[E, A]
 */
object toyModel {

  /**
   * Используя executable encoding реализуем свой zio
   *
   */
  case class ZIO[-R, +E, +A](run: R => Either[E, A]){ self =>
    def map[B](f: A => B): ZIO[R, E, B] = ZIO(r => self.run(r).map(f))
    def flatMap[R1 <: R, E1 >: E, B](f: A => ZIO[R1, E1, B]): ZIO[R1, E1, B] =
      ZIO(r => self.run(r).fold(zio.ZIO.fail, f).run(r))
  }

  /**
   * Реализуем конструкторы под названием effect и fail
   */

  object ZIO {
    def effect[A](value: => A): ZIO[Any, Throwable, A] = {
      try {
        ZIO(_ => Right(value))
      } catch {
        case e => ZIO(_ => Left(e))
      }

      def fail[E](e: E): ZIO[Any, E, Nothing] = ZIO(_ => Left(e))
    }

    val echo: ZIO[Any, Throwable, Unit] = for {
      str <- ZIO.effect(StdIn.readLine())
      _ <- ZIO.effect(println(str))
    } yield ()

    echo.run()
  }

  object zioTypeAliases {
    type Error
    type Environment

    // ZIO[-R, +E, +A]

    lazy val _: Task[Int] = ??? // ZIO[Any, THROWABLE, INT]
    lazy val _: IO[Error, Int] = ??? //ZIO[Any, Error, Int]
    lazy val _: RIO[Environment, Int] = ??? // ZIO[Env, Nothing, Int]
    lazy val _: URIO[Environment, Int] = ??? //ZIO[Env, Nothing, Int]
    lazy val _: UIO[Int] = ??? //ZIO[Any, Nothing, Int]
  }

  object zioConstructors {

    // константа
    val _: UIO[Int] = zio.ZIO.succeed(7)

    // любой эффект
    val _: Task[Unit] = ZIO.effect(println("Hello"))

    // любой не падающий эффект
    val _: UIO[Unit] = zio.ZIO.effectTotal(println("hello"))

    // From Future
    val f: Future[Int] = ???
    val _: Task[Int] = zio.ZIO.fromFuture(ec => f)

    // From try
    val t: Try[String] = ???
    val _: Task[String] = zio.ZIO.fromTry(t)

    // From either
    val e: Either[String, Int] = ???
    val _: IO[String, Int] = zio.ZIO.fromEither(e)

    // From Options
    val opt: Option[Int] = ???
    val z: IO[Option[Nothing], Int] = zio.ZIO.fromOption(opt)
    val zz: UIO[Option[Int]] = z.option
    val _: ZIO[Any, Option[Nothing], Int] = zz.some

    // From function
    val _: URIO[Int, Int] = zio.ZIO.fromFunction[String, Unit](str => println(str))

    // Особые версии конструкторов
    val _: UIO[Unit] = zio.ZIO.unit
    val _: UIO[Option[Nothing]] = zio.ZIO.none
    val _: UIO[Nothing] = zio.ZIO.never // while(true)
    val _: zio.ZIO[Any, Nothing, Nothing] = zio.ZIO.die(new Throwable("Died"))
    val _: zio.ZIO[Any, Int, Nothing] = zio.ZIO.fail((7))

    lazy val readLine: Task[String] = zio.ZIO.effect(StdIn.readLine())
//    lazy val lineToInr: zio.ZIO[Any, Throwable, Int] =
//    lazy val lineToInr: zio.ZIO[Any, Throwable, Int] =


  }



}

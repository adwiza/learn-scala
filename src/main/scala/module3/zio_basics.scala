package module3

//import zio.{Clock, IO, RIO, Task, UIO, URIO, ZIO}
import zio._
import zio.clock.Clock
import zio.console._

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
//  case class ZIO[-R, +E, +A](run: R => Either[E, A]){ self =>
//    def map[B](f: A => B): ZIO[R, E, B] = ZIO(r => self.run(r).map(f))
//    def flatMap[R1 <: R, E1 >: E, B](f: A => ZIO[R1, E1, B]): ZIO[R1, E1, B] =
//      ZIO(r => self.run(r).fold(zio.ZIO.fail, f).run(r))
//  }

  /**
   * Реализуем конструкторы под названием effect и fail
   */

//  object ZIO {
//    def effect[A](value: => A): ZIO[Any, Throwable, A] = {
//      try {
//        ZIO(_ => Right(value))
//      } catch {
//        case e => ZIO(_ => Left(e))
//      }
//
//      def fail[E](e: E): ZIO[Any, E, Nothing] = ZIO(_ => Left(e))
//    }
//
//    val echo: ZIO[Any, Throwable, Unit] = for {
//      str <- ZIO.effect(StdIn.readLine())
//      _ <- ZIO.effect(println(str))
//    } yield ()
//
//    echo.run()
//  }

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
    val _: UIO[Unit] = ZIO.effectTotal(println("hello"))

    // From Future
    val f: Future[Int] = ???
    val _: Task[Int] = ZIO.fromFuture(ec => f)

    // From try
    val t: Try[String] = ???
    val _: Task[String] = ZIO.fromTry(t)

    // From either
    val e: Either[String, Int] = ???
    val _: IO[String, Int] = zio.ZIO.fromEither(e)

    // From Options
    val opt: Option[Int] = ???
    val z: IO[Option[Nothing], Int] = zio.ZIO.fromOption(opt)
    val zz: UIO[Option[Int]] = z.option
    val _: ZIO[Any, Option[Nothing], Int] = zz.some

    // From function
    val _: URIO[Int, Int] = ZIO.fromFunction[String, Unit](str => println(str))

    // Особые версии конструкторов
    val _: UIO[Unit] = zio.ZIO.unit
    val _: UIO[Option[Nothing]] = zio.ZIO.none
    val _: UIO[Nothing] = zio.ZIO.never // while(true)
    val _: zio.ZIO[Any, Nothing, Nothing] = zio.ZIO.die(new Throwable("Died"))
    val _: zio.ZIO[Any, Int, Nothing] = zio.ZIO.fail((7))

    lazy val readLine: Task[String] = zio.ZIO.effect(StdIn.readLine())
    lazy val lineToInt: zio.ZIO[Any, Throwable, Int] = readLine.flatMap(str => zio.ZIO.effect(str.toInt))

    val a1: Task[Int] = ???
    val b1: Task[String] = ???

    val _: zio.ZIO[Any, Throwable, Int] = zio.ZIO.effect(println("Hello")) *> zio.ZIO.effect(1 + 1)

    lazy val _: zio.ZIO[Any, Throwable, (Int, String)] = a1.zip(b1)

    // zipRight
    lazy val _: zio.ZIO[Any, Throwable, String] = a1 zipRight b1  // a1 *> b1

    //zip Left
    lazy val _: zio.ZIO[Any, Throwable, Unit] = a1 zipLeft b1 // a1 <* b1

    val r1: zio.ZIO[Any, Throwable, Int] = for {
      n1 <- lineToInt
      n2 <- lineToInt
    } yield n1 + n2

    lazy val ab4: zio.ZIO[Any, Throwable, String] = b1.zipWith(b1)(_ + _) // zipWith

    lazy val c: ZIO[Clock, Throwable, Int] = zio.ZIO.effect("").as(7)

  }

  object zioRecursion {

    /**
     * Read int from console and retry if mistake
     */

    lazy val readInt: ZIO[Console, Throwable, Int] =
      ZIO.effect(StdIn.readLine()).flatMap(str => ZIO.effect(str.toInt))


    lazy val readIntOrRetry: ZIO[Console, Throwable, Int] = readInt.orElse(
      ZIO.effect(println("Error, repeat please")) *> readIntOrRetry
    )

    def factorial(n: Int): Int =
      if (n <= 1 ) n
      else n * factorial(n - 1)
    def factorialZ(n: BigDecimal): Task[BigDecimal] = {
          if (n <= 1 ) ZIO.succeed(n)
          else ZIO.succeed(n).zipWith(factorialZ(n - 1))(_ * _)
    }

  }



}

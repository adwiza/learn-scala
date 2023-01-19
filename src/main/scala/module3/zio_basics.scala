//package module3
//
//import zio.{Clock, Console, IO, RIO, Task, UIO, URIO, ZIO}
//
//import scala.concurrent.Future
//import scala.io.StdIn
//import scala.util.Try
//
///**
// * ZIO[-R, +E, +A] ----> R => Ether[E, A]
// */
//object toyModel {
//
//  /**
//   * Используя executable encoding реализуем свой zio
//   *
//   */
//  case class ZIO[-R, +E, +A](run: R => Either[E, A]){ self =>
////    def map[B](f: A => B): ZIO[R, E, B] = ZIO(r => self.run(r).map(f))
////    def flatMap[R1 <: R, E1 >: E, B](f: A => ZIO[R1, E1, B]): ZIO[R1, E1, B] =
////      ZIO(r => self.run(r).fold(zio.ZIO.fail, f).run(r))
//    def foldM[R1 <: R, E1, B](
//                             failure: E => ZIO[R1, E1, B],
//                             success: A => ZIO[R1, E1, B]
//                             ): ZIO[R1, E1, B] = {
//    ZIO(r =>
//      self
//        .run(r)
//        .fold(failure, success)
//        .run(r))}
//
//    def orElse[R1 <: R, E1, A1 >: A](other: ZIO[R1, E1, A1]): ZIO[R1, E1, A1] =
//      foldM(
//        _ => other,
//        v => ZIO(_ => Right(v))
//      )
//
//    def option: ZIO[R, Nothing, Option[A]] =
//      foldM(
//        _ => ZIO(_ => Right(None)),
//        v => ZIO(_ => Right(Some(v)))
//      )
//
//    def mapError[E1](f: E => E1): ZIO[R, E1, A] = foldM(
//      e => ZIO(r => Left(f(e))),
//      v => ZIO(r => Right(v))
//    )
//}
//
//  /**
//   * Реализуем конструкторы под названием effect и fail
//   */
//
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
//
//  object zioTypeAliases {
//    type Error
//    type Environment
//
//    // ZIO[-R, +E, +A]
//
//    lazy val _: Task[Int] = ??? // ZIO[Any, THROWABLE, INT]
//    lazy val _: IO[Error, Int] = ??? //ZIO[Any, Error, Int]
//    lazy val _: RIO[Environment, Int] = ??? // ZIO[Env, Nothing, Int]
//    lazy val _: URIO[Environment, Int] = ??? //ZIO[Env, Nothing, Int]
//    lazy val _: UIO[Int] = ??? //ZIO[Any, Nothing, Int]
//  }
//
//  object zioConstructors {
//
//    // константа
//    val _: UIO[Int] = zio.ZIO.succeed(7)
//
//    // любой эффект
//    val _: Task[Unit] = zio.ZIO.attempt(println("Hello"))
//
//    // любой не падающий эффект
//    val _: UIO[Unit] = zio.ZIO.attempt(println("hello"))
//
//    // From Future
//    val f: Future[Int] = ???
//    val _: Task[Int] = zio.ZIO.fromFuture(ec => f)
//
//    // From try
//    val t: Try[String] = ???
//    val _: Task[String] = zio.ZIO.fromTry(t)
//
//    // From either
//    val e: Either[String, Int] = ???
//    val _: IO[String, Int] = zio.ZIO.fromEither(e)
//
//    // From Options
//    val opt: Option[Int] = ???
//    val z: IO[Option[Nothing], Int] = zio.ZIO.fromOption(opt)
//    val zz: UIO[Option[Int]] = z.option
//    val _: ZIO[Any, Option[Nothing], Int] = zz.some
//
//    // From function
//    val _: URIO[Int, Int] = ZIO.environmentWith[String](str => println(str))
//
//    // Особые версии конструкторов
//    val _: UIO[Unit] = zio.ZIO.unit
//    val _: UIO[Option[Nothing]] = zio.ZIO.none
//    val _: UIO[Nothing] = zio.ZIO.never // while(true)
//    val _: zio.ZIO[Any, Nothing, Nothing] = zio.ZIO.die(new Throwable("Died"))
//    val _: zio.ZIO[Any, Int, Nothing] = zio.ZIO.fail((7))
//
//    lazy val readLine: Task[String] = zio.ZIO.attempt(StdIn.readLine())
//    lazy val lineToInt: zio.ZIO[Any, Throwable, Int] = readLine.flatMap(str => ZIO.attempt(str.toInt))
//
//    val a1: Task[Int] = ???
//    val b1: Task[String] = ???
//
//    val _: zio.ZIO[Any, Throwable, Int] = ZIO.attempt(println("Hello")) *> ZIO.attempt(1 + 1)
//
//    lazy val _: zio.ZIO[Any, Throwable, (Int, String)] = a1.zip(b1)
//
//    // zipRight
//    lazy val _: zio.ZIO[Any, Throwable, String] = a1 zipRight b1  // a1 *> b1
//
//    //zip Left
//    lazy val _: zio.ZIO[Any, Throwable, Unit] = a1 zipLeft b1 // a1 <* b1
//
//    val r1: zio.ZIO[Any, Throwable, Int] = for {
//      n1 <- lineToInt
//      n2 <- lineToInt
//    } yield n1 + n2
//
//    lazy val ab4: zio.ZIO[Any, Throwable, String] = b1.zipWith(b1)(_ + _) // zipWith
//
//    lazy val c: ZIO[Clock, Throwable, Int] = zio.ZIO.attempt("").as(7)
//
//  }
//
//  object zioRecursion {
//
//    /**
//     * Read int from console and retry if mistake
//     */
//
//    lazy val readInt: ZIO[Any, Throwable, Int] =
//      ZIO.attempt(StdIn.readLine()).flatMap(str => ZIO.attempt(str.toInt))
//
//
//    lazy val readIntOrRetry: ZIO[Any, Throwable, Int] = readInt.orElse(
//      ZIO.attempt(println("Error, repeat please")) *> readIntOrRetry
//    )
//
//    def factorial(n: Int): Int =
//      if (n <= 1 ) n
//      else n * factorial(n - 1)
//    def factorialZ(n: BigDecimal): Task[BigDecimal] = {
//          if (n <= 1 ) ZIO.succeed(n)
//          else ZIO.succeed(n).zipWith(factorialZ(n - 1))(_ * _)
//    }
//
//  }
//
//}

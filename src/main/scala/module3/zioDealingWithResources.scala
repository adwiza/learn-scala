package module3

import breeze.storage.ConfigurableDefault.fromV
import jdk.internal.net.http.common.Utils
import module3.toyModel2.User
import module3.tryFinally.traditional.{Resource, acquareResource}
import module3.zipBracket.{File, openFile}
import org.apache.hadoop.shaded.org.apache.commons.configuration2.tree.QueryResult
import org.scalatest.prop.Configuration
import sun.security.provider.NativePRNG.Blocking
import zio.Console.printLine
import zio.Random.nextIntBetween
import zio.Scope.global
import zio.{Clock, Config, Console, IO, RIO, Random, Scope, Task, UIO, URIO, ZIO, ZLayer}
import zio.ZIO.{Release, acquireReleaseWith, blockingExecutor, fromAutoCloseable}
import zio.managed.ZManaged

import java.io._
import java.lang.Thread.sleep
import javax.management.Query
import javax.validation.constraints.Email
import scala.concurrent.Future
import scala.sys.process.BasicIO
import scala.util.{Failure, Success}

object tryFinally {

  object traditional {
    trait Resource

    lazy val acquareResource: Resource = ???

    def use(resource: Resource): Unit = ???

    def releaseResource(resource: Resource): Unit = ???

    lazy val result1 = {
      val res = acquareResource
      try {
        use(res)
      } finally {
        releaseResource(res)
      }
    }

    def withResource[R <: Closeable](resource: => R)(use: R => Any): Unit = {
      try {
        use(resource)
      } finally {
        resource.close()
      }
    }
  }

  object future {
    implicit val global = scala.concurrent.ExecutionContext.global

    lazy val acquireFutureResource: Future[Resource] = ???

    def use(resource: Resource): Future[Unit] = ???

    def releaseResource(resource: Resource): Future[Unit] = ???

    def ensuring[A](f: Future[A])(release: Future[Any]): Future[A] =
      f.transformWith {
        case Failure(exception) => release.flatMap(_ => Future.failed(exception))
        case Success(value) => release.flatMap(_ => Future.successful(value))
      }

    lazy val result2Future = ???

    val _ = acquireFutureResource.flatMap { r =>
      ensuring(use(r))(releaseResource(r))
    }
  }
}
  object zipBracket{

    trait File {
      def name: String
      def close: Unit = println(s"File -${name}- closed")
      def readLines: List[String] = List("Hello world", "Scala is cool")
    }
    object File {
      def apply(_name: String): File = new File{
        override def name: String = _name
      }
      def apply(_name: String, lines: List[String]): File = new File {
        override def name: String = _name

        override def readLines: List[String] = lines
      }
    }
    def openFile(fileName: String): IO[IOException, File] =
      ZIO.fromEither(Right(File(fileName)))
    def openFile(fileName: String, lines: List[String]): IO[IOException, File] =
      ZIO.fromEither(Right(File(fileName, lines)))

//    def closeFile(file: File): UIO[Unit] = URIO(file.close)
//
//    def handleFile(file: File): ZIO[Console, Nothing, List[Unit]] = {
//      val lines: List[String] = file.readLines
//      ZIO.foreach(lines)(printLine)
//    }
//
//    val twoFiles: ZIO[Console, IOException, List[Unit]] =
//      ZIO.acquireReleaseWith(openFile("test1"))(closeFile){ f1 =>
//        ZIO.acquireReleaseWith(openFile("test1"))(closeFile){ f2 =>
//          handleFile(f1) *> handleFile(f2)
//      }
//
//  }
}

object zioZManaged {
  //  lazy val files: ZManaged[Any, IOException, List[File]] =
  //    ZManaged.foreachPar(fileNames)(file)

  //  val live: ZLayer[Configuration with Blocking, Throwable, DBTransactor] = ZLayer.fromManaged(
  //    (for {
  //      config <- zio.config.getConfig[Config].toManaged_
  //      executor <- blockingExecutor.toManaged_
  //      transactor <- DBTransactor.mkTransactor(config.db, executor.asEC, executor.asEC)
  //    } yield transactor)
  //  )
  trait ZIO[-R, +E, +A] {
    def provide(r: R): IO[E, A]
  }

  object ZIO {
    def environment[R]: ZIO[R, Nothing, R] = ???
  }

  type MyEnv = Clock with Console with RandomAccessFile

//  def e1: ZIO[Clock with Console with Random, Nothing, Unit]

//  def e2: ZIO[MyEnv, Nothing, Unit]

//  trait DBService{
//    def tx[T](query: Query[T]): IO[DBError, QueryResult[T]]
//  }
//  trait EmailService{
//    def makeEmail(email: String, body: String): Task[Email]
//    def sendEmail(email: Email): Task[Unit]
//  }
//
//  trait LoggingService{
//    def log(str: String): Task[Unit]
//  }
//
//  val dbService: DBService = ???
//  val emailService: EmailService = ???
//
//  val combined: DBService with EmailService = ???
//
//  type MyEnv
  import zio.Console.printLine

  def e1: ZIO[Clock with Console, Nothing, Nothing] = for {
  console <- ZIO.environment[Console].map(_.get)
  clock <- ZIO.environment[Clock].map(_.get)
  random <- ZIO.environment[Random].map(_.get)
  _ <- printLine("Hello")
  _ <- sleep(5)
  int <- nextIntBetween(1, 10)
  _ <- printLine(int.toString)
  } yield ()

  val queryAndNotify = for {
    dbService <- ZIO.environment[DBService]
    emailService <- ZIO.environment[EmailService]
    email <- emailService.makeEmail("", "")
    query: Query[User] = ???
    result <- dbService.sendEmail(email)
  } yield ???

  lazy val services: DBService with EmailService = ???

  lazy val e3: IO[Any, Nothing] =  queryAndNotify.provide(services)
}


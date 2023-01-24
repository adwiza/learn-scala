package module3

import module3.tryFinally.traditional.{Resource, acquareResource}
import module3.zipBracket.File
import zio.Console.printLine
import zio.{Console, IO, RIO, UIO, URIO, ZIO}
import zio.ZIO.{Release, acquireReleaseWith}
import zio.managed.ZManaged

import java.io.{Closeable, IOException}
import scala.concurrent.Future
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
  import zio.managed._

  val file1: ZManaged[Any, IOException, File] = ???

  val file2: ZManaged[Any, IOException, File] = ???
}

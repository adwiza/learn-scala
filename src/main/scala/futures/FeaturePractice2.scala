package futures
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Try

object FeaturePractice2 extends App{

  // 1 implicit ctxc
  // 2 srict

  val x = 1 + 1
  (x + x) // 1 + 1


//  val eval = Future(println(1))

//  (eval, eval) // 1 + 1, 1 + 1

  // 3 -> project Loom
  Future.successful(1).map(_ + 1)

  val list: List[Future[Int]] = List(Future(10), Future(20), Future(30))

  val ff: Future[List[Int]] = Future.sequence(list)
  val fft: Future[List[Int]] = Future.traverse(List(1, 2, 3)) { x =>
    Future(x + x)
  }

  val fz: Future[(Int, Int)] = Future(10).zip(Future(100))

  val fut1 = Future.successful {
    Thread.sleep(100)
//    println("1")
    1
  }

  val fut2 = Future.successful {
    Thread.sleep(200)
//    println("2")
    2
  }

  val fres = for {
    x1 <- fut1.recover {case exception: Exception =>
    println("1 step " + exception.getMessage)
    throw exception
    }
    x2 <- fut2.recoverWith { case exception: Exception =>
      println("2 step " + exception.getMessage)
      Future.failed(exception)
    }
    x3 <- Future(1/0).recover { case exception: Exception =>
      println("3 step " + exception.getMessage)
      0
    }
    x4 <- fut2
  } yield x1 + x2

  Await.ready(fres, Duration.Inf)


}

package futures


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
object FuturesPractice extends App {

  // -- unit
  val f1: Future[Int] = Future.successful {
    //throw new Exception("ex1")

    Thread.sleep(100)
//    println("thread: " + Thread.currentThread().getName)
//     println("succeed")


    1
    }

  // != unit
  val f2 = Future {
    throw new Exception("ex1")
    Thread.sleep(100)
//    println("thread: " + Thread.currentThread().getName)
//    println("apply")
    1
  }// asynchronous
//  Await.result(f2, 1.second)
//  Await.ready(f2, 1.second)

  f2
//  println(s"$f2 finished")

  case class Context(message: String)

  def printContext(implicit ctx: Context): Unit =
    println(ctx.message)

  implicit val ctx = Context("Hello implicit")

//  printContext // Hello implicit
  val time = System.currentTimeMillis()

  val fut1 = Future.successful{
    Thread.sleep(1000)
    println("1")
    1
  }
  println(s"first: " + (System.currentTimeMillis() - time))

  val fut2 = Future.successful {
    Thread.sleep(2000)
    println("2")
    2
  }

  println(s"medium: " + (System.currentTimeMillis() - time))

  def f(i: Int): Future[String] = Future.successful{
    println("f")
    i.toString
  }

   val fres =  for  {
      x1 <- fut1
      x2 <- fut2
    } yield x1 + x2

  Await.ready(fres, 10.second)

  println(s"finished: " + (System.currentTimeMillis() - time))

}


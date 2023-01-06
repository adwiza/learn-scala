package futures

import java.util.concurrent.Executors
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object ExecutionCtx extends App {

//  implicit val exec = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
//  val tp = Executors.newSingleThreadExecutor()
//  val tp = Executors.newFixedThreadPool(2)
  val tp = Executors.newWorkStealingPool(2)
  implicit val exec = ExecutionContext.fromExecutor(tp)

    // ExecCtx ~ Threat pool
//  val ex = new ExecutionContext {
//    override def execute(runnable: Runnable): Unit = ???
//
//    override def reportFailure(cause: Throwable): Unit = ???
//  }

  val time = System.currentTimeMillis()

  val fut1 = Future {
    Thread.sleep(1000)
    println("1")
    1
  }

  val fut2 = Future {
    Thread.sleep(2000)
    println("2")
    2
  }

  val fres = for {
    x1 <- fut1
    x2 <- fut2
  } yield x1 + x2

  Await.ready(fres, Duration.Inf)

  println(s"finished: " + (System.currentTimeMillis() - time))

}

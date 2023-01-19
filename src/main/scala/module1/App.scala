package module1

//import module3.funceffect.{declarativeEncoding, executableEncoding}

import zio.Cause.Both
import zio._
import zio.Console.printLine
import zio.Exit.unit.getOrThrowFiberFailure

import scala.language.{existentials, implicitConversions, postfixOps}
object App {

  def main(args: Array[String]): Unit = {
    sealed trait NotificationError
    case object NotificationByEmailFailed extends NotificationError
    case object NotificationBySMSFailed extends NotificationError

    val z1 = zio.ZIO.fail(NotificationByEmailFailed)
    val z2 = zio.ZIO.fail(NotificationBySMSFailed)

    val app = z1.zipPar(z2).tapErrorCause {
      case Both(left, right) =>
//        printLine(left.failureOption.toString) *> printLine(right.failureOption.toString)
//        printLine(left.prettyPrint) *> printLine(right.prettyPrint)
        printLine(left.toString) *> printLine(right.toString)
    }.orElse(printLine("app is failed"))

    Unsafe.unsafe { implicit unsafe => Runtime.default.unsafe.run(app).getOrThrowFiberFailure() }
  }
}

package me.chuwy.otusbats

import cats.data.{NonEmptyList, Validated, State}
trait Show[A] {
  def show(a: A): String
}
object Show {
  // 1.1 Instances
  val a: NonEmptyList[String] = NonEmptyList(null, Nil)
  val e: Either[String, Int] = ???

  val result: Validated[String, (Int, Int, Int)] = ???

  case class BState[S, A](f: S => (S, A))

  type Candy = String

  case class VendingMachine(candies: List[String])

  var initial: VendingMachine = VendingMachine(Nil)

  val giveCandy: State[VendingMachine, Option[Candy]] = {
    State { machine =>
      machine.candies match {
        case Nil => (VendingMachine(Nil), None)
        case candy :: rest => (VendingMachine(rest), Some(candy))
      }
    }
  }

  val tripleCandy = for {
    _ <- giveCandy
    _ <- giveCandy
    result <- giveCandy
  } yield result

  val (updated, candy) = tripleCandy.run(initial).value

  val isThereACandy: State[VendingMachine, Boolean] =
    tripleCandy.map(_.isDefined )


}

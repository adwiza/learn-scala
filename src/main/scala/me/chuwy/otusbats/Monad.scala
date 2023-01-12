package me.chuwy.otusbats

import scala.collection.immutable.Set
import cats.implicits._

trait Monad [F[_]] extends Functor[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = {
    flatten(this.map(fa){ a => f(a) })
  }

  def point[A](a: A): F[A]

  def flatten[A](fa: F[F[A]]): F[A] =
    this.flatMap(fa)(identity)

}

object Monad {
  val a: Option[Option[Int]] = ???
  val b: Set[Int] = ???
  //  a.map()
  val list: List[Int] =
    List(1, 2, 3, 4, -5, -1, 3)
//  list.traverse { int => if (int < 0) None else Some(int) }
  list.traverse { int => if (int < 0) List(int) else List(int-1, int, int+1)}

  import cats.data.Validated

  list.traverse { int => if (int < 0) int.invalidNel else int.validNel}


}

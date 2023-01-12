package me.chuwy.otusbats

trait Functor [F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]

}

object Functor {
  // 1. Instances
  implicit val optionF: Functor[Option] = new Functor[Option] {
    def map[A, B](fa: Option[A])(f: A => B): Option[B] =
      fa.map(f)
  }
  implicit val listF: Functor[List] = new Functor[List] {
    def map[A, B](fa: List[A])(f: A => B): List[B] =
      fa.map(f)
  }

  val optionList: List[Option[Int]] = ???

  optionList.map {opt => opt.map(_.toString)}

  // 2. Combinations

  def nested[F[_]: Functor, G[_]: Functor, A, B](fga: F[G[A]])(f: A => B): F[G[B]] =
    implicitly[Functor[F]].map(fga) { ga =>
      implicitly[Functor[G]].map(ga)(f)

    }
}
package me.chuwy.otusbats

trait Semigroup[A] {
  def combine(x: A, y: A): A
}
object Semigroup {
  // (a |+| b) |+| c == a |+| (b |+| c)
  implicit val intSemigroup: Semigroup[Int] =
    new Semigroup[Int] {
      def combine(x: Int, y: Int): Int = x + y
    }

}

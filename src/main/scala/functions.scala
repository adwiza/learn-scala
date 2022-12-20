package module1
object functions {
  def sum (x: Int, y: Int): Int = {
    x + y
  }

  val sum2: (Int, Int) => Int = (x, y) => x + y

  sum(1, 2)
  sum2(1, 2)

//  val list = List(sum2, sum2)

//  list(0)(1, 2) // 3
  val list: List[(Int, Int) => Int] = List(sum _, sum _)

  def bar(f: (Int, Int) => Int): Int = ???

  bar(sum2)
  bar(sum)

  def isEven(v: Int): Boolean = v % 2 == 0

  val xx = sum2
  val xz: (Int, Int) => Int = sum
  val xy: Int => Int => Int = xz.curried

  val _: Int = xy(2)(2)

}

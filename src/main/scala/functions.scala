package module1
object functions {
  def sum (x: Int, y: Int): Int = {
    x + y
  }

  val sum2: (Int, Int) => Int = (x, y) => x + y

  sum(1, 2)
  sum2(1, 2)

  val list = List(sum2, sum2)

  list(0)(1, 2) // 3
}

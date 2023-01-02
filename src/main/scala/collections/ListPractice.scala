package collections

object ListPractice extends App{

  val list = 1 :: 2:: Nil
  val list2 = List(1, 2) // equal up

  List(List(1), List(2), List(1, 2)) match {
    case x :: y :: List(xs, ys) :: zs => println("match")
  }

  val res: List[(Int, Int)] = for {
    a <- list
    b <- list
    if (b % 2 == 0)
  } yield (a, b)

  // Desugared code
  val res3: List[Tuple2[Int, Int]] = list
    .flatMap((a: Int) =>
      list
        .withFilter((b: Int) => (b.%(2).==(0)))
        .map((b: Int) => Tuple2.apply(a, b))
    )

  val res2: List[Tuple2[Int, Int]] = list
    .flatMap((a: Int) =>
      list
        .map((b: Int) => Tuple2.apply(a, b))
    )

  list.foreach(println)
//  println(list.flatMap(i => List(i, i, i)))
//  println(list.map(i => List(i, i, i)))
//  println(list.map(i => List(i, i, i)).flatten)

  /**
   * Реализуем метод конкатинации двух листов,
   * не используя vat и мутабельные структуры
   */
  def concat[T](xs: List[T], ys: List[T]) = ???

  /**
   * Реализация квадратов через map
   */
  def squareList(xs: List[Int]): List[Int] = {
    xs.map(x => x * x)
  }

  /**
   * Другая, часто используемая операция это фильтрация по определенному
   * критерию
   */
  def positiveElements(xs: List[Int]): List[Int] = xs match {
    case Nil => xs
    case y :: ys =>
      if (y > 0) y :: positiveElements(ys)
      else positiveElements(ys)
  }
}

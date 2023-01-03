package collections

import scala.collection.immutable.{HashMap, HashSet}

object LazyListExample extends App{

  def repeat: LazyList[Int] = {
    LazyList[Int](1, 2) #::: (repeat)
  }

  repeat.take(10).foreach(println)

}

object VectorExample extends App {
  val m = Vector[Int](1, 2, 3)

  print(m.updated(0, 10))
}
object HashMapExample extends App {
  val t: (String, Int) = "1" -> 2
  val map = Map("1" -> 2, "2" -> 2)

  map.map(pair => (pair._1, pair._2))
  map.map {case (a, b) => (a, b)}
  map.flatMap { case (a, b) => Map(a -> b) }

  map.get("1")
  map("1")

}

object SetExample extends App {
  val s: Set[Int] = Set(1, 2, 3)
  val ss: Set[Int] = HashSet(1, 2, 3)

  println(s.toList)
  println(s.contains(1))


}
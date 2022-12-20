package module1

import scala.math.Equiv.Unit

object variables extends App {

  // Constant declaration
  val M: String = "Month"
  val Y: String = "Year"

  // Variable declaration
  var c: Int = 0

  // this block summarize 3 + 2 and put the result in x variable after that the result will multiply 2
  val block: Int = {
    val x = 3 + 2
    x * 2
  }: Int
//  println(block)

  val cond: Boolean = true

  val x1: String = if (cond) "yes" else "false"

//  val x2: Unit = if (cond) println("yes") else println("false")

//  val x3: Any = if (cond) println("yes") else "false"

  var c5: Boolean = false
  val x5: Unit = while (c5) {
    1 + 1
    println()
  }
  do {
    1 + 1
  } while (c5)

//  for (el <- List(1, 2, 3)) {
//    println(el)
//  }
//  for (el <- 0 to 10) {
//    println(el)
//  }

  val arr: Array[Int] = Array(0, 1, 3, 4, 5, 6, 7, 8, 9, 10)
//  for (idx <- arr.indices) {
//    println(arr(idx))
//  }
//  for (idx2 <-0 to arr.size -1){
//    println(arr(idx2))
//  }
  for (idx3 <- 0 until 10) {
    println(arr(idx3))
  }
}

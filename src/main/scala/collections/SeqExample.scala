package collections

import spire.ClassTag

object SeqExample extends App{
  val s: Seq[Int] = Seq(1, 2, 3)
  val l: Seq[Int] = List(1, 2, 3)
  val ll: Seq[Int] = 1 :: 2 :: 3 :: Nil
  val lll: List[Int] = 1 :: 2 :: 3 :: Nil
  val v: Seq[Int] = Vector(1, 2, 3)

//  lll.toSeq
//  ll.toList

  v match {
    //case x :: xs => println("cons")
    //case List(x, xs @ _*) => println("list")
    case 1 :: 2 :: 3 :: Nil => println("cons")
    case List(1, 2, 3) => println("cons")
    case Seq(x, xs @ _*) => println("seq")

  }

  case class A(i: Int)
  case class B(s: String)

//  val a: A = B("b").asInstanceOf[A]
  val aa: List[A] = List(B("b")).asInstanceOf[List[A]]

  val vv: List[Int] = List(1, 2, 3)
//  vv match {
//    case l: List[String] => println("List str")
//    case l: List[Int] => println("List int")
//  }

  // def matchList[T](list: List[T])(implicit c: ClassTag[T]) equal below
  def matchList[T: ClassTag](list: List[T]) = list match {
    case List(_: String, xs @ _*) => println("List str")
    case List(_: Int, xs @ _*) => println("List int")
  }
//  matchList(vv)

  val ran = List.range(1, 10, 2)
  val fil = List.fill(10)(1) //

//  println(ran)
//  println(fil)

//  println(ran.find(_ == 7)) // find element in list
//
//  println(ran.headOption) // head of list

  ran.headOption.map(_ + 1).getOrElse()
  ran.headOption.fold(0)(_ + 1)

  List(1, 2, 3).foldLeft[Int](0){
    case (acc, current) => acc + current
  }

//  println(List(1, 2, 3).foldLeft[Int](0) (_ + _))
//  println(List(1, 2, 3).sum)

  // FoldLeft
  ((1 + 2) + 3)

  // FoldRight
  (1 + (2 + 3))

  println( ("1" :: "2" :: Nil).foldRight[String]("")(_ + _))

//  println(s)
//  println(l)
//  println(ll)
//  println(lll)
}

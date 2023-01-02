package collections

object ListExample extends App {

  sealed   trait LinkedList[+T]{

    def :: [B >: T](elem: B): LinkedList[B] = new ::(elem, this)
    def ++ [T1 >: T](another: LinkedList[T1]): LinkedList[T] = {
      this match {
        case Nil => another.asInstanceOf[LinkedList[T]]
        case head :: tail => head :: tail ++ (another)
       }
    }

    def map[B](f: T => B): LinkedList[B] = {
      this match {
        case Nil => Nil.asInstanceOf[LinkedList[B]]
        case head :: tail => f(head) :: tail.map(f)
      }
    }
    def filter(pred: T => Boolean): LinkedList[T] = {
      this match {
        case Nil => Nil.asInstanceOf[LinkedList[T]]
        case head :: tail =>
          if (pred(head)) head :: tail.filter(pred)
          else tail.filter(pred)
      }
    }

    def flatMap[B](f: T => LinkedList[B]): LinkedList[B] = {
      this match {
        case Nil => Nil.asInstanceOf[LinkedList[B]]
        case head :: tail =>
          val b: LinkedList[B] = f(head)
          b ++ tail.flatMap(f)
      }
    }
  }
  case class ::[T](head: T, tail: LinkedList[T]) extends LinkedList[T]
  case object Nil extends LinkedList[Nothing]

  val ls: LinkedList[String] = "1" :: Nil

  val li: LinkedList[Int] = 1 :: Nil

//  println(li.flatMap(i => i ::(i :: Nil)))

  val lifm: LinkedList[Int] = li.flatMap(i => i ::(i :: Nil))
  val lim : LinkedList[LinkedList[Int]] = li.map(i => i ::(i :: Nil))

  val myList: LinkedList[(Int, Int)] = for {
    l <- li
    ll <- li
  } yield (l, ll)

  myList.map(println)
}

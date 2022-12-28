package module1

import module1.referential_transparency.Abiturient
import org.apache.hadoop.shaded.org.eclipse.jetty.util.Scanner.Notification

import java.util.UUID
import scala.annotation.tailrec

/**
 * referential transparency
 */

object referential_transparency {
  case class Abiturient(id: String, email: String, fio: String)

  type Html = String

  sealed trait Notification

  object Notification {
    case class Email(email: String, text: Html) extends Notification

    case class Sms(telephone: String, msg: String) extends Notification
  }

  case class AbiturientDTO(email: String, fio: String, password: String)

  trait NotificationService {
    def sendNotification(notification: Notification): Unit
  }

  trait AbiturientService {
    def registerAbiturient(abiturientDTO: AbiturientDTO): Abiturient
  }

  class AbiturientServiceImpl(notificationService: NotificationService) extends AbiturientService {
    override def registerAbiturient(abiturientDTO: AbiturientDTO): Abiturient = {
      val abiturient = Abiturient(UUID.randomUUID().toString, abiturientDTO.email, abiturientDTO.fio)
      notificationService.sendNotification(Notification.Email(abiturient.email, "Some message"))
      abiturient
    }

    // referential transparency
    def registerAbiturient2(abiturientDTO: AbiturientDTO): (Abiturient, Notification) = {
      val abiturient = Abiturient(UUID.randomUUID().toString, abiturientDTO.email, abiturientDTO.fio)
      (abiturient, Notification.Email(abiturient.email, "Some message"))
    }
  }

  val t: (Abiturient, Notification) = ???
  t._1 // Abiturient
  t._2 // Notification
}
  // recursion
object recursion {

    /**
     * Реализация метода вычисления n!
     * n! = 1 * 2 * ... n
     */

    def fact(n: Int): Int = {
      var _n = 1
      var i = 2
      while (i <= n) {
        _n *= i
        i += 1
      }
      _n
    }

    def fact2(n: Int): Int = {
      if (n <= 1) 1
      else n * fact2(n - 1)
    }

    def fact3(n: Int): Int = {
      @tailrec // Проверяет на наличие хвостовой рекурсии
      def loop(n1: Int, acc: Int): Int = {
        if (n1 <= 1) acc
        else loop(n1 - 1, n1 * acc) // Хвостовая рекурсия
      }

      loop(n, acc = 1)
    }

    /**
     * реализация вычисления числа Фибоначчи
     * F0 = 0, F1 = 1, Fn = Fn - 1 + Fn - 2
     *
     */
    def fib(n: Int): Int = fib(n - 1) + fib(n - 2)
  }
object hof{
  def printFactorialResult(r: Int): Unit = println(s"Factorial result is ${r}")
  def printFibonacciResult(r: Int): Unit = println(s"Fibonacci result is ${r}")
  def printResult(r: Int, name: String): Unit = println(s"$name result is ${r}")
  def printResult[A, B](f: A => B, v: A, name: String): Unit =
    println(s"$name result is ${f(v)}")

  // Follow type implementation
  def partial[A,B,C](a: A, f: (A, B) => C): B => C = (b: B) => f(a, b)

  def sum(x: Int, y: Int): Int = ???
  val r: Int => Int = partial(1, sum)

  r(2) // sum(1, 2)
}

object opt {
  /**
   *
   * Реализация типа option, который будет указывать на присутствие либо отсутствие результата
   */
  // Animal
  // Dog extend Animal
  // Option[Dog] Option[Animal] if "+" present
  sealed trait Option[+A]{
    def isEmpty: Boolean = this match {
      case Option.Some(_) => false
      case Option.None => true
    }
    def get: A = this match {
      case Option.Some(v) => v
      case Option.None => throw new Exception("Get on empty list")
    }

    def getOrElse[B >: A](b: B): B = this match {
      case Option.Some(v) => v
      case Option.None => b
    }
    def map[B](f: A => B): Option[B] = this match {
      case Option.Some(v) => Option.Some(f(v))
      case Option.None => Option.None
    }
    // val i: Option[Int] i.map(v => v + 1)

    def findAbiturient(id: String): Option[Abiturient]

    def f(x: Int, y: Int): Option[Int] =
      if(y == 0) Option.None
      else Option.Some(x / y)

    def flatMap[B](f: A => Option[B]): Option[B]

  }
  object Option{
    case class Some[A](v: A) extends Option[A]
    case object None extends Option[Nothing]
  }
}

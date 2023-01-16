package module3

import module1.type_system.v

import scala.io.StdIn

object funceffect extends App{
  val greet = {
    println("Как тебя зовут?")
    val name = StdIn.readLine()
    println(s"Привет $name")
  }


  val askForAge = {
    println("How old are you?")
    val age = StdIn.readInt()
    if (age > 18) println("Access allowed")
    else (println("Access denied"))
  }


  object declarativeEncoding {
    /**
     * 1. Объявить декларативную модель Console
     */
    sealed trait Console[+A]
    case class Println[A](string: String, rest: Console[A]) extends Console[A]
    case class ReadLine[A](str: String => Console[A]) extends Console[A]
    case class Return[A](value: () => A) extends Console[A]

    val p1 = Println("Как тебя зовут?",
      ReadLine(str =>
      Println(s"Привет, $str",
        Return(() => ())))
    )

    /**
     * 2. Написать конструкторы
     */
    object Console {
      def succeed[A](v: => A): Console[A] = Return(() => v)
      def printLine(str: String): Console[Unit] = Println(str, succeed())
      def readLine: Console[String] = ReadLine(str => succeed(str))
    }

    /**
     * 3. Описать желаемую программу с помощью нашей модели
     */

    /** 4. Написать операторы
     *
     */
    object consoleOps {
      implicit class ops[A](self: Console[A]){
        def map[B](f: A => B): Console[B] = flatMap(v => Console.succeed(f(v)))
        def flatMap[B](f: A => Console[B]): Console[B] = self match {
          case Println(string, rest) => Println(string, rest.flatMap(f))
          case ReadLine(ff) => ReadLine(str => ff(str).flatMap(f))
          case Return(value) => f(value())
        }
      }
    }

    import consoleOps._

//    val p2 =

  }

}

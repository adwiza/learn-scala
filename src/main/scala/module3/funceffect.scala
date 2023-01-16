package module3

import module1.adt.case_classes.name
import module1.type_system.v

import scala.io.StdIn

object funceffect extends App{
//  val greet = {
//    println("Как тебя зовут?")
//    val name = StdIn.readLine()
//    println(s"Привет $name")
//  }


//  val askForAge = {
//    println("How old are you?")
//    val age = StdIn.readInt()
//    if (age > 18) println("Access allowed")
//    else (println("Access denied"))
//  }


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

    val p2: Console[Unit] = for {
      _ <- Console.printLine("What is your name?")
      name <- Console.readLine
      _ <- Console.printLine(s"Hello $name")
    } yield ()

    val p3: Console[Unit] = for {
      _ <- Console.printLine("How old are you?")
      age <- Console.readLine
      _ <- if (age.toInt >= 18) Console.printLine(s" Access allowed")
      else Console.printLine("Access denied")
    } yield ()

    def interpret[A](console: Console[A]): A = console match {
      case Println(str, rest) =>
        println(str)
        interpret(rest)
      case ReadLine(f) =>
        interpret(f(StdIn.readLine()))
      case Return(v) => v()
    }

    def describe[A](console: Console[A]): Unit = ???

    val p4 = for {
      _ <- p2
      _ <- p3
    } yield ()
  }

  object executableEncoding {

    case class Console[A](unsafeRun: () => A, describe: () => ()) { self =>
      def map[B](f: A => B): Console[B] = {
        flatMap(f.andThen(b => Console.console(b)))
      }
      def flatMap[B](f: A => Console[B]): Console[B] =
        Console.console(f(self.unsafeRun()).unsafeRun())

      def cond[B](predicate: A => Boolean)(success: Console[B])(faulure: Console[B]) = ???

    }

    object Console{
      def console[A](a: => A): Console[A] = Console(() => a)
      def printLine(str: String): Console[Unit] = Console(() => println(str))
      def readLine(): Console[String] = Console(() => StdIn.readLine())

    }

    lazy val p: Console[Unit] = for {
      _ <- Console.printLine("What is your name?")
      name <- Console.readLine
      _ <- Console.printLine(s"Hello $name")
    } yield ()
  }

}

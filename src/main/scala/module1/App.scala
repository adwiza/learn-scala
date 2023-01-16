package module1

import module3.funceffect.{declarativeEncoding, executableEncoding}

import scala.language.{existentials, postfixOps}
object App {

  def main(args: Array[String]): Unit = {
//    declarativeEncoding.interpret(declarativeEncoding.p4)
    executableEncoding.p.unsafeRun()
  }
//    def sumItUp: Int = {
//      def one(x: Int): Int = {
//        return x
//        1
//      }
//
//      val two = (x: Int) => {
//        return x
//        2
//      }
//      1 + one(2) + two(3)
//    }
//    println("Hello world")
// //     println(sumItUp)
//    println(type_system.int)
//    println(type_system.boolean)
//    println(type_system.unit)
//    println(type_system.cc)
//    println(type_system.FooBar)
//
//    println(type_system.v1.foo())
//  }
}


package module1

import shapeless.ops.tuple.Length

import java.io.Closeable

object type_system {
  /**
   * Scala type system
   */
  // AnyVal

  //
  var int: Int = _
  var boolean: Boolean = _
  var unit: Unit = _

  // Unit
  def foo(): Unit = {
    1 + 1 // Не имеет смысла
    println("Nothing to return")
  }

  // Null

  // Nothing

  def absurd(v: Nothing): Nothing = ???

  // Generics

  def ensureClose [T <: Closeable](o: T)(action: T => Unit): Unit = {
    try {
      action(o)
    } finally {
      o.close()
    }
  }
//  val io: OutputStream = ???
//  ensureClose(io) {str =>
//    str.write(1)
//  }

  /**
   *
   * class
   *
   *
   */
  class Foo(var v1: Int, var v2: Int){
    def foo: Unit = println(v1 + v2)
    def this(x: Int) = this(0, x)
  }

  val f = new Foo(v1 = 1, v2 = 2)
  val f2 = new Foo(1)

  class Rectangle(val length: Int, val width: Int) {
    def this (length:Int) = this(length, length)
    def perimeter: Int=length * 2 + width * 2
    def area: Int = length * width
  }

  class Foo2{
    private var _foo: String = _
    def foo: String = _foo // getter
    def foo_=(s: String): Unit = _foo = s //setter
  }

  val f3 = new Foo2

  f3.foo // getter
  f3.foo = "x" // setter

  /**
   * object
   *
   * 1. Single tone
   * 2. Lazy initialisation
   */
  object FooBar{
    println("foobar")
  }

  // val fb: FooBar // object
//  val fb: FooBar.type = ??? // type

  /**
   * case classes
   *
   */

  // создать case класс кредитная карта с двумя полями номер и сvc

  case class CreditCard(number: String, cvc: Int)

  val cc = CreditCard("23423424242424", 123)
  val cc3 = CreditCard("23423424242424", 123)
  cc == cc3 // true
  val cc2 = cc.copy(cvc = 345)

  class CreditCard2 private(number: String, cvc: Int){

  }
  object CreditCard2{
//    def create(number: String) = new CreditCard2(number, cvc = 123 )
    def apply(number: String, cvc: Int) = new CreditCard2(number, cvc = 123 )
  }

  CreditCard2("", 123)


  /**
   * case object
   *
   *
   * Используется для создания перечислений или же в качестве сообщений для Акторов
   */

    trait Color
    case object Red extends Color
    case object Green extends Color
    case object Blue extends Color

  /**
   * trait
   *
   */

    trait X {
    def x: Int
    val y: String = "y"
  }

  val _ = new X{
    override def x: Int = ???
  }

  trait Y extends X {
    override def x: Int = ???
    override val y: String = ""
  }

  sealed trait FF

  class A {
    def foo() = "A"
  }

  trait B extends A {
    override def foo(): String = "B" + super.foo()
  }
  trait C extends B {
    override def foo(): String = "C" + super.foo()
  }
  trait D extends A {
    override def foo(): String = "D" + super.foo()
  }
  trait E extends C {
    override def foo(): String = "E" + super.foo()
  }

  val v =  new A with D with C with  B
  // CBDA
  // A -> D -> B -> C
  val v1 = new A with E with D with C with B

  /**
   * Value classes and universal traits
   */

  trait Printable extends Any{
    def print: Unit = println(this)
  }
  case class Id(id: String) extends AnyVal with Printable
  val id = Id("1")
}

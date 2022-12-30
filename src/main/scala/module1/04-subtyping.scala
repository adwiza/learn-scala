package module1

import module1.type_system.CreditCard
import org.apache.spark.sql.catalyst.expressions.StartsWith

import java.time.YearMonth

object subtyping {

  /**
   * Type Operators
   */

  trait Vehicle
  trait Car extends Vehicle
  trait Moto extends Vehicle
  object Harley extends Moto
  object Mustang extends Car

  type IsSubtypeOf[A, B >: A]
  type IsSupertypeOf[A, B <: A]

  /**
   *
   * С помощью типа IsSubtypeOf выражаем отношение  Car and Vehicle
   *
   */

  val t1 : IsSubtypeOf[Car, Vehicle] = ???

  val t2 : IsSubtypeOf[Mustang.type, Car] = ???

  val t3 : IsSupertypeOf[Vehicle, Harley.type] = ???

  def IsInstanceOf[A, B >: A](a: A): Unit = ???

  lazy val mustCompile1 = IsInstanceOf[Mustang.type, Car](Mustang)
  lazy val mustCompile2 = IsInstanceOf[Harley.type, Moto](Harley)
//  lazy val mustNotCompile1 = IsInstanceOf[Mustang.type, Moto](Mustang)
//  lazy val mustNotCompile2 = isInstanceOf[Mustang.type, Car](Harley)

  // Вариантность
  class Garage[T]

  //Вариантность полей
  // val
  class Garage2[+T](val vehicle: T)

//  val hondaShadow = new Moto {}
//  val shadowGarage = new Garage2[Vehicle](hondaShadow)
//  val carGarage: Garage2[Car] = shadowGarage
//  val _: Car = carGarage.vehicle // Car

  // var
//  class Garage3[-T](var vehicle: T)

  val hondaShadow = new Moto {}
//  val shadowGarage = new Garage3[Vehicle](hondaShadow)

//  val _ = shadowGarage.vehicle = new Car {}

  // var m: Moto = ???
  // m = new Car {}  // так делать нельзя
  class Garage4[-T] {
    def put(vehicle: T): Unit = ???
  }

//  val garageVehicle: Garage4[Vehicle] = new Garage4[Car]
//  val _ = garageVehicle.put(new Moto {})

  // method return value
  abstract class Garage5[+T]{
    def get: T
  }
//  val garageVehicle = new Garage5[Vehicle] {
//    override def get: Vehicle = new Moto {}
//  }
//
//  val carGarage: Garage5[Car] = garageVehicle
//
//  val _ = carGarage.get // Car  т.е. клали мотоцикл а получаем машину - не верно
  abstract class Cleaner[-T]{
    def clean(t: T)
  }

  val cleaner: Cleaner[Vehicle] = new Cleaner[Vehicle] {
    override def clean(t: Vehicle): Unit = println("Cleaned")
  }
  def cleaner[T](t: T, cleaner: Cleaner[T]) = cleaner.clean(t)

  cleaner[Car](new Car {}, cleaner)

  val l: List[Vehicle] = List(new Car {}, new Moto {})

  val head: Vehicle = l.head // Vehicle

  trait  Box[+T]{
    def get: T
  }

  // val a : isSubtypeOf = ???

  trait Consumer[+T]{
    def comsume[TT >:T](v: TT): Unit
    def produce: T
  }

  trait Consumer2[-T] {
    def comsume(v: T): Unit
    def produce[TT <: T]: TT
  }
}

object adt {
  object tuples {

    // boolean * Unit
    // true * Unit
    // false * Unit

    /**
     * Tuples
     */
    type ProductUnitBoolean = (Unit, Boolean)

    val c1: ProductUnitBoolean = ((), true)
    val c2: ProductUnitBoolean = ((), false)


    /**
     * Реализовать тип Person который будет содержать имя и возраст
     */
//    type Person = (String, Int)
//
//    val person: Person = ("John", 21)
//
//    type CreditCard = (String, java.time.YearMonth, String, Short)
//
//    val card: CreditCard = ("2342342342342", 2015-12,  "Aleksei El", 123)
  }

  object case_classes {
    /**
     * Case classes
     */

    case class Person(val name: String, val age: Int)

    // Создать экземпляр для Tony Stark 42 года
    lazy val tonyStark = Person("Tony Stark", 42)

    // Pattern matching
    val a: Any = ???

    a match {
      case Int => println("Int")
      case String => println("String")
      case _ => println("default")
    }

    object Person {
      def unapply(p: Person): Some[(String, Int)] = Some((p.name, p.age))
    }
    val (c, d) = (1, "Hello")
    val Person(name, age) = tonyStark
    val person = Person("", 4)
    def printNameAndAge: Unit = tonyStark match {
      case Person(name, age) => println(s"$name $age")
    }
    final case class Employee(name: String, address: Address)
    final case class Address(street: String, number: Int)

    val alex = Employee("Alex", Address("XXX", 221))

    alex match {
      case Employee(_, address) => println(address.number)
      case Employee(_, Address(_, number)) => println(number)
    }
    alex match {
      case Employee("Alex", _) => println("alex")
      case _ => println("no alex")
    }
    alex match {
      case Employee(name, _) if name == "Alex" => println("alex")
      case _ => println("no alex")
    }
    alex match {
      case Employee(name, _) if name.startsWith("Alex") => println("alex")
      case _ => println("no alex")
    }
    alex match {
      case e @ Employee(name, _) => println(e.address.street)
      case _ => println("no alex")
    }
  }
  sealed trait A
  case object B extends A
  case object C extends A
  case object D extends A

  val a: A = ???

//  a match {
//    case B | C => println("B or C")
//    case D => println("D")
//  }
  val Alex = "Alex"
  val str: String = ???


  str match {
    case Alex => println("Alex")
    case _ => println("no Alex")
  }
  object either {

    /**
     * Sum
     */
    // Unit and Boolean () | true | false

//    sealed trait Either[T]
//    case class Left[T](v: T) extends Either[T]
//    case class Right[T](v: T) extends Either[T]

    type IntOrString = Either[Int, String]
    val intOrString: IntOrString = Left(1)
    val intOrString2: IntOrString = Right("hello")

    type PaymentMethod = Either[CreditCard, Either[WireTransfer, Cash]]

    final case class CreditCard()
    final case class WireTransfer()
    final case class Cash ()
  }
  object seated_traits {

    sealed trait Card
    object Card {
      // Suits of cards
      final case class Clubs(points: Int) extends Card // clubs
      final case class Diamonds(points: Int) extends Card // diamonds
      final case class Spades(points: Int) extends Card // spades
      final case class Hearts(points: Int) extends Card // hearts
    }
    lazy val card: Card = Card.Spades(10)
  }
}

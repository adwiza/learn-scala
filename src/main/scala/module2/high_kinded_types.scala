package module2

import module2.high_kinded_types.JsValue.{JsNull, JsNumber, JsString}

object high_kinded_types extends App {

//  def tuple[A, B](a: List[A], b: List[B]): List[(A, B)] =
//    a.flatMap{ a =>  b.map((a, _))}
//
//  def tuple[A, B](a: Option[A], b: Option[B]): Option[(A, B)] =
//    a.flatMap{ a =>  b.map((a, _))}
//
//  def tuple[E, A, B](a: Either[E, A], b: Either[E, B]): Either[E, (A, B)] =
//    a.flatMap{ a =>  b.map((a, _))}
//
//  def tuplef[F[_], A, B](fa: F[A], b: F[B]): F[(A, B)] = ???
//
//  trait Bindable[F[_], +A] {
//    def map[B](f: A => B): F[B]
//    def flatMap[B](f: A => F[B]): F[B]
//  }
//
//  def tupleBindable[F[_], A, B](fa: Bindable[F, A], fb: Bindable[F, B]): F[(A, B)] =
//    fa.flatMap{ a =>  fb.map((a, _))}
//
//  def listBindable[A](list: List[A]): Bindable[List, A] = new Bindable[List, A] {
//    override def map[B](f: A => B): List[B] = list.map(f)
//    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
//  }
//  def optBindable[A](list: Option[A]): Bindable[Option, A] = new Bindable[Option, A] {
//    override def map[B](f: A => B): Option[B] = list.map(f)
//    override def flatMap[B](f: A => Option[B]): Option[B] = list.flatMap(f)
//  }

  trait Bindable[F[_]]{
    def map[A, B](el: F[A])(f: A => B): F[B]
    def flatMap[A, B](al: F[A])(f: A => F[B]): F[B]
  }
  def tupleF[F[_]: Bindable, A, B](fa: F[A], fb: F[B]): F[(A, B)] = {
//    val ev = implicitly[Bindable[F]]
    Bindable[F].flatMap(fa)(a => Bindable[F].map(fb)(b => (a, b)))
  }

  object Bindable  {

    def apply[F[_]](implicit ev: Bindable[F]): Bindable[F] = ev

    implicit val opt = new Bindable[Option] {
      override def map[A, B](el: Option[A])(f: A => B): Option[B] =
        el.map(f)
      override def flatMap[A, B](el: Option[A])(f: A => Option[B]): Option[B] =
        el.flatMap(f)
    }
//    implicit val list: Bindable[List] = ???
  }

  val optA: Option[Int] = Some(1)
  val optB: Option[Int] = Some(2)

  val list1 = List(1, 2, 3)
  val list2 = List(4, 5, 6)

//  println(tupleBindable(optBindable(optA), optBindable(optB)))
//  println(tupleBindable(listBindable(list1), listBindable(list2)))

  println(tupleF(optA, optB))
//  println(tupleF(list1, list2))

  sealed trait JsValue
  object JsValue{
    final case class JsObject(get: Map[String, JsValue]) extends JsValue
    final case class JsString(get: String) extends JsValue
    final case class JsNumber(get: Double) extends JsValue
    final case object JsNull extends JsValue
  }

  trait JsonWriter[T]{
    def write(v: T): JsValue
  }

  object JsonWriter {
    def apply[T](implicit jsonWriter: JsonWriter[T]): JsonWriter[T] = jsonWriter

    implicit val str: JsonWriter[String] = new JsonWriter[String] {
      def write(v: String): JsValue = JsString(v)
    }
    implicit val int: JsonWriter[Int] = new JsonWriter[Int] {
      def write(v: Int): JsValue = JsNumber(v)
    }

    implicit def opt[A](implicit jsonWriter: JsonWriter[A]) =
      new JsonWriter[Option[A]] {
        override def write(v: Option[A]): JsValue = v match {
          case Some(v) => jsonWriter.write(v)
          case None => JsNull
        }
      }
  }

  object JsonSyntax {
    implicit class jsonOps[A](val v: A) extends AnyVal {
      def toJson(implicit ev: JsonWriter[A]): JsValue = ev.write(v)
    }
  }

  import JsonSyntax._

  "dsfsdfs".toJson
  Option(1).toJson
  Option("sdfs").toJson
  1.toJson
}

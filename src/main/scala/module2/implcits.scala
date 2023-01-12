package module2

import scala.language.implicitConversions

object implcits extends App {

  // implicits conversions

  /**
   *
   * Расширить возможности типа string, методом  trimToOption, который возповзает Option[String]
   * если строка пустая иои null,  то None
   * если нет. то Some от строки мо всеми начальными и конечными пробелами
   */

    implicit def stringToOps(str: String): StringOps = new StringOps(str)

    class StringOps(string: String){
      def trimToOption: Option[String] = Option(string).flatMap{ str =>
        val trimed = str.trim
        if(trimed.isEmpty) None
        else Some(trimed)
      }
    }


  val s1 = "FppBar".trimToOption
  val s2 = "".trimToOption
  val s3 = " ".trimToOption

  // implicit conversions ОПАСНЫ

//  implicit def strToUbt(str: String): Int = Integer.parseInt(str)

  // "Foobar" / 2

  implicit val seq = Seq("a", "b", "c") // val f: Int => String

  def log(str: String): Unit = println(str)

  log(str =  42)


}

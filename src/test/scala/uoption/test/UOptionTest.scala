package uoption.test

// this import is the price to pay to use UOptions
import uoption._

import org.scalactic._
import org.scalatest._
import org.scalactic.TypeCheckedTripleEquals._

object UOptionTest {
  private def areEqualImpl[A](a: UOption[A], b: Any)(
      implicit innerEq: Equality[A]): Boolean = {
    if (a eq UNone) {
      b.asInstanceOf[AnyRef] eq UNone
    } else {
      (b.asInstanceOf[AnyRef] ne UNone) &&
      innerEq.areEqual(a.get, b.asInstanceOf[UOption[A]].get)
    }
  }

  /** This custom equality makes sure that Some(5) is considered different from
   *  5, by forwarding types everywhere.
   */
  implicit def uoptionEquality[A](
      implicit innerEq: Equality[A]): Equality[UOption[A]] = {
    new Equality[UOption[A]] {
      def areEqual(a: UOption[A], b: Any): Boolean = areEqualImpl(a, b)
    }
  }

  implicit def usomeEquality[A](
      implicit innerEq: Equality[A]): Equality[USome[A]] = {
    new Equality[USome[A]] {
      def areEqual(a: UOption[A], b: Any): Boolean = areEqualImpl(a, b)
    }
  }

  implicit val unoneEquality: Equality[UNone.type] = {
    new Equality[UNone.type] {
      def areEqual(a: UNone.type, b: Any): Boolean = a eq b.asInstanceOf[AnyRef]
    }
  }
}

class UOptionTest extends FunSuite with Matchers {
  import UOptionTest._

  test("sanity") {
    println(implicitly[Equality[USome[Int]]])
    assert((UNone === UNone) == true)
    assert(((UNone: UOption[Int]) === (UNone: UOption[String])) == true)
    assert((USome(5) === USome(5)) == true)
    assert((USome(5) === USome(6)) == false)
    assert((USome(5) === 5) == false)
    assert((USome(5) === UNone) == false)
    assert((USome(UNone) === UNone) == false)
    assert((USome(USome(UNone)) === USome(None)) == false)
    assert((USome(USome(5)) === USome(5)) == false)
  }

  test("isEmpty") {
    assert(UNone.isEmpty == true)
    assert(USome(5).isEmpty == false)
    assert(USome(UNone).isEmpty == false)
    assert(USome(USome(UNone)).get.isEmpty == false)
    assert(USome(USome(UNone)).get.get.isEmpty == true)
  }

  test("isDefined") {
    assert(UNone.isDefined == false)
    assert(USome(5).isDefined == true)
    assert(USome(UNone).isDefined == true)
    assert(USome(USome(UNone)).get.isDefined == true)
    assert(USome(USome(UNone)).get.get.isDefined == false)
  }

  test("getOrElse") {
    assert(UNone.getOrElse(5) === 5)
    assert(UNone.getOrElse(UNone) == UNone)
    assert(UNone.getOrElse(USome(true)) == USome(true))

    assert(USome(3).getOrElse(5) == 3)
    assert(USome(USome(42)).getOrElse(5) === USome(42))
    assert(USome(UNone).getOrElse(42) == UNone)
    assert(USome(USome(UNone)).getOrElse(42) === USome(UNone))
  }

  test("UNone.map") {
    val noneInt: UOption[Int] = UNone
    assert(noneInt.map(_ + 1) == UNone)
  }
}

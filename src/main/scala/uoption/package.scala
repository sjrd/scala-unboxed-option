import scala.language.higherKinds

package object uoption /*extends UOptionDefinition*/ {
  private[uoption] sealed case class WrappedNone(depth: Int)(
      val unwrap: Any // UNone or WrappedNone
  ) {
    lazy val wrap: WrappedNone =
      WrappedNone(depth + 1)(this)

    private val stringRepr: String =
      ("USome(" * depth) + "UNone" + (")" * depth)

    override def toString(): String = stringRepr
  }

  object UNone {
    private[uoption] val wrap: WrappedNone = WrappedNone(1)(this)
  }

  type UOption[+A] >: UNone.type <: AnyRef

  type USome[+A] <: UOption[A]

  object USome {
    @inline // only for Scala.js?
    def apply[A](value: A): USome[A] = value match {
      case value @ UNone      => value.wrap.asInstanceOf[USome[A]]
      case value: WrappedNone => value.wrap.asInstanceOf[USome[A]]
      case _                  => value.asInstanceOf[USome[A]]
    }

    // UOptionOps fits the contract of name-based pattern matching
    def unapply[A](option: UOption[A]): UOptionOps[A] =
      new UOptionOps(option)
  }

  implicit class UOptionOps[A](val __self: UOption[A]) extends AnyVal {
    @inline def isEmpty: Boolean = __self.asInstanceOf[AnyRef] eq UNone
    @inline def isDefined: Boolean = !isEmpty

    /** Must not be called when `isEmpty` is `true`! */
    @inline // only for Scala.js?
    def forceGet: A = (__self: Any) match {
      case none: WrappedNone =>
        none.unwrap.asInstanceOf[A]
      case _ =>
        __self.asInstanceOf[A]
    }

    @inline // is this a good idea at all?
    def get: A =
      if (isEmpty) throw new NoSuchElementException("UNone.get")
      else forceGet

    @inline def map[B](f: A => B): UOption[B] =
      if (isEmpty) __self.asInstanceOf[UOption[B]]
      else USome(f(forceGet))

    @inline def getOrElse[B >: A](ifEmpty: => B): B =
      if (isEmpty) ifEmpty
      else forceGet
  }
}

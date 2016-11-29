package uoption

import scala.language.higherKinds

abstract class UOptionDefinition {
  type UOption[+A] >: UNone.type <: AnyRef

  type USome[+A] <: UOption[A]
}

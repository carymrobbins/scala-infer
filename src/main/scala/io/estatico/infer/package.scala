package io.estatico

package object infer {

  /**
   * Represents an inferred type.
   * In hopes to avoid clashing with an existing type member, let's
   * just name our "inferred" type some generated UUID.
   */
  type Infer = { type Inferred_4D488932_5A36_4AF7_B8EC_0FCE39436A68 }
  object Infer {
    /** Simplifies constructing an `Infer` type. */
    type Aux[A] = Infer { type Inferred_4D488932_5A36_4AF7_B8EC_0FCE39436A68 = A }
  }

  /** Type function for getting the inferred type from an `Infer` */
  type Inferred[A <: Infer] = A#Inferred_4D488932_5A36_4AF7_B8EC_0FCE39436A68

  /**
   * Implicit conversion for converting any value to an `Infer`
   * Compile with optimization to eliminate this call at runtime.
   */
  @inline implicit def toInfer[A](a: A): Infer.Aux[A] = a.asInstanceOf[Infer.Aux[A]]

  /**
   * Automatically convert the `Infer` value to its original type.
   * Compile with optimization to eliminate this call at runtime.
   */
  @inline def unInfer(i: Infer): Inferred[i.type] = i.asInstanceOf[Inferred[i.type]]
}

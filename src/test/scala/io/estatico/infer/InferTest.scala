package io.estatico.infer

import org.scalatest.{FlatSpec, Matchers}

class InferTest extends FlatSpec with Matchers {

  import InferTest._

  "cast" should "get type information from the inferencer" in {
    cast[Object]("foo") should be(a[String])
    cast[String]("foo") should be(a[String])
    cast[java.lang.Number](1: java.lang.Integer) should be(a[java.lang.Integer])
    trait Foo
    case object Bar extends Foo
    cast[Foo](Bar) should be(a[Bar.type])
  }

  it should "fail at compile time where A is not <:< B" in {
    assertDoesNotCompile("""cast[Int]("foo")""")
    assertDoesNotCompile("""cast[Float](2)""")
    trait Foo
    case object Baz
    assertDoesNotCompile("""cast[Foo](Baz)""")
  }

  "Key.make" should "work with inferred types" in {
    trait Foo
    implicit val keyFooString: Key.Aux[Foo, String] = Key.instance("foo:" + _)
    trait Bar
    implicit val keyBarInt: Key.Aux[Bar, Int] = Key.instance(i => "bar:" + (i * 2).toString)
    Key.make[Foo]("hey") shouldEqual "foo:hey"
    Key.make[Bar](12) shouldEqual "bar:24"
  }
}

object InferTest {

  /**
   * An example method which can be created using Infer.
   * Safe cast to `B` when the argument passed as `a` extends from `B`.
   * No need to specify the type of `a` in the type params as we can use `Infer` instead.
   */
  def cast[B](a: Infer)(implicit ev: Inferred[a.type] <:< B): B = a.asInstanceOf[B]

  /**
   * An example type class which can utilize our Infer mechanism when being
   * summoned, inferring the `Input` type.
   */
  trait Key[A] {
    type Input
    def make(input: Input): String
  }
  object Key {

    type Aux[A, I] = Key[A] { type Input = I }

    def apply[A](implicit k: Key[A]): Aux[A, k.Input] = k

    def instance[A, I](f: I => String): Aux[A, I] = new Key[A] {
      type Input = I
      def make(input: Input): String = f(input)
    }

    def make[A](input: Infer)(implicit ev: Aux[A, Inferred[input.type]]): String
      = ev.make(unInfer(input))
  }
}

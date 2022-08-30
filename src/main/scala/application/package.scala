import cats.effect.kernel.{CancelScope, Poll}
import cats.effect.{IO, MonadCancel}

package object globals {

  import application.models.errors.SpawnPokerGameError
  import cats.data.Validated
  import cats.implicits._
  import cats.{Applicative, Monad}

  type Validation[T] = Validated[SpawnPokerGameError, T]

  object Validation {

    def right[T](e: T): Validation[T] =
      Right(e).toValidated

    def left[T](e: SpawnPokerGameError): Validation[T] =
      Left(e).toValidated

    implicit val applicativeInst: Applicative[Validation] = new Applicative[Validation] {
      override def pure[A](x: A): Validation[A] = right(x)

      override def ap[A, B](ff: Validation[A => B])(fa: Validation[A]): Validation[B] =
        ff.andThen(f => fa.map(a => f(a)))
    }

    implicit val monadInst: Monad[Validation] = new Monad[Validation] {
      override def flatMap[A, B](fa: Validation[A])(f: A => Validation[B]): Validation[B] =
        fa.andThen(a => f(a))

      override def tailRecM[A, B](a: A)(f: A => Validation[Either[A, B]]): Validation[B] = ???

      override def pure[A](x: A): Validation[A] = right(x)
    }

  }

}

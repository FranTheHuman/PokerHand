package application

import application.models.errors.SpawnPokerGameError
import cats.data.Validated
import cats.implicits._
import cats.{Applicative, Monad}

object Validators {

  type Validation[T] = Validated[SpawnPokerGameError, T]

  object Validation {

    def right[T](e: T): Validation[T] =
      Right(e).toValidated

    def left[T](e: SpawnPokerGameError): Validation[T] =
      Left(e).toValidated

    implicit def applicativeInst: Applicative[Validation] = new Applicative[Validation] {
      override def pure[A](x: A): Validation[A] = right(x)

      override def ap[A, B](ff: Validation[A => B])(fa: Validation[A]): Validation[B] =
        ff.andThen(f => fa.map(a => f(a)))
    }

    implicit def monadInst: Monad[Validation] = new Monad[Validation] {
      override def flatMap[A, B](fa: Validation[A])(f: A => Validation[B]): Validation[B] =
        fa.andThen(a => f(a))

      override def tailRecM[A, B](a: A)(f: A => Validation[Either[A, B]]): Validation[B] = ???

      override def pure[A](x: A): Validation[A] = right(x)
    }

  }

  val CARD_REGEX = "[2-9KTQJA]{1}[CDSH]{1}"

  val validateInputConsistency: List[String] => Validation[List[String]] =
    str =>
      Either
        .cond(
          str.map(_.matches(CARD_REGEX)).count(_ == true).equals(str.size),
          str,
          SpawnPokerGameError(s"Invalid values in the input -> $str")
        )
        .toValidated

  val validateInputLength: String => Validation[List[String]] =
    str =>
      Either
        .cond(
          str.length >= 25,
          str.split(" ").toList,
          SpawnPokerGameError(s"Invalid length input for game -> $str")
        )
        .toValidated

  val validateParametersLength: List[String] => Validation[List[String]] =
    parameters =>
      Either
        .cond(
          parameters.size >= 3,
          parameters,
          SpawnPokerGameError(s"Invalid length of params for play the game -> $parameters")
        )
        .toValidated

  val validateHandCards: (String, Int) => Validation[String] =
    (hand, length) =>
      Either
        .cond(
          hand.length == length,
          hand,
          SpawnPokerGameError(
            s"Invalid cards length of hand for the type game \n LENGTH: ${hand.length} \n REQUIRED LENGTH: $length"
          )
        )
        .toValidated

}

package application

import application.models.errors.SpawnPokerGameError
import cats.implicits._
import globals.Validation

object Validations {

  val CARD_REGEX = "([2-9KTQJA]{1}[cdsh]{1})*"

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

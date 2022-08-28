package application

import application.errors.SpawnPokerGameError

object Validators {

  val CARD_REGEX = "[2-9KTQJA]{1}[CDSH]{1}"

  type Validation[T] = Either[SpawnPokerGameError, T]

  val validateInputConsistency: List[String] => Validation[List[String]] =
    str =>
      Either
        .cond(
          str.map(_.matches(CARD_REGEX)).count(_ == true).equals(str.size),
          str,
          SpawnPokerGameError(s"Invalid values in the input -> $str")
        )

  val validateInputLength: String => Validation[List[String]] =
    str =>
      Either
        .cond(
          str.length >= 25,
          str.split(" ").toList,
          SpawnPokerGameError(s"Invalid length input for game -> $str")
        )

  val validateParametersLength: List[String] => Validation[List[String]] =
    parameters =>
      Either
        .cond(
          parameters.size >= 3,
          parameters,
          SpawnPokerGameError(s"Invalid length of params for play the game -> $parameters")
        )

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

}

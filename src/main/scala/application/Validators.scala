package application

import application.errors.SpawnPokerGameError

object Validators {

  type Validation[T] = Either[SpawnPokerGameError, T]

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

}

package application

import application.Validators.Validation
import application.errors.SpawnPokerGameError
import application.models.Card.toCards
import application.models.PokerGame.{FiveCardDraw, withBoardCards}
import application.models.{Hand, PokerGame, PokerGamesNames}

trait GameSpawner extends PokerGamesNames {

  def spawn: String => Validation[PokerGame]

  val toPokerGame: (Option[String], List[String]) => Validation[PokerGame] =
    (maybeType, strings) =>
      maybeType match {
        case Some(TEXAS_HOLDEM)   => withBoardCards(strings)
        case Some(OMAHA_HOLDEM)   => withBoardCards(strings)
        case Some(FIVE_CARD_DRAW) => Right(FiveCardDraw(strings.map(toCards).map(Hand)))
        case Some(_)              => Left(SpawnPokerGameError("Poker Game Type Not Match"))
        case None                 => Left(SpawnPokerGameError("Poker Game Type Empty"))
      }

}

object GameSpawner extends GameSpawner {

  override def spawn: String => Validation[PokerGame] =
    str =>
      for {
        inputs <- Validators.validateInputLength(str)
        params <- Validators.validateParametersLength(inputs)
        game   <- toPokerGame(params.headOption, params.tail)
      } yield game

}

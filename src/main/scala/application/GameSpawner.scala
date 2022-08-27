package application

import application.Validators.Validation
import application.errors.SpawnPokerGameError
import application.models.Hand.makeHands
import application.models.PokerGame.{FiveCardDraw, OmahaHoldem, TexasHoldem}
import application.models.names.PokerGamesNames
import application.models.{Hand, PokerGame}

trait GameSpawner {

  def spawn: String => Validation[PokerGame]

}

object GameSpawner extends GameSpawner with PokerGamesNames {

  override def spawn: String => Validation[PokerGame] =
    str =>
      for {
        inputs <- Validators.validateInputLength(str)
        params <- Validators.validateParametersLength(inputs)
        game   <- create(params.headOption, params.tail)
      } yield game

  private def create(typeGame: Option[String], inputs: List[String]): Validation[PokerGame] =
    typeGame match {
      case Some(TEXAS_HOLDEM) =>
        Right(TexasHoldem(Hand(inputs.headOption.getOrElse("")), makeHands(inputs)))
      case Some(OMAHA_HOLDEM) =>
        Right(OmahaHoldem(Hand(inputs.headOption.getOrElse("")), makeHands(inputs)))
      case Some(FIVE_CARD_DRAW) =>
        Right(FiveCardDraw(makeHands(inputs)))
      case Some(_) =>
        Left(SpawnPokerGameError("Poker Game Type Not Match"))
      case None =>
        Left(SpawnPokerGameError("Poker Game Type Empty"))
    }

}

package application

import application.Validators.Validation
import application.errors.SpawnPokerGameError
import application.models.Hand.makeHands
import application.models.PokerGame.{FiveCardDraw, OmahaHoldem, TexasHoldem}
import application.models.names.PokerGamesNames
import application.models.{Hand, PokerGame}
import cats.implicits.toTraverseOps

trait GameSpawner {

  def spawn: String => Validation[PokerGame]

}

object GameSpawner extends GameSpawner with PokerGamesNames {

  override def spawn: String => Validation[PokerGame] =
    str =>
      for {
        inputs      <- Validators.validateInputLength(str)
        validInputs <- Validators.validateInputConsistency(inputs)
        params      <- Validators.validateParametersLength(validInputs)
        game        <- createByType(typeGame = params.headOption, cards = params.tail)
      } yield game

  private def createByType(typeGame: Option[String], cards: List[String]): Validation[PokerGame] =
    typeGame match {

      case Some(TEXAS_HOLDEM) =>
        for {
          validBoardCards <- Validators.validateHandCards(cards.headOption.getOrElse(""), 10)
          validCards      <- cards.tail.map(cards => Validators.validateHandCards(cards, 4)).sequence
        } yield TexasHoldem(Hand(validBoardCards), makeHands(validCards))

      case Some(OMAHA_HOLDEM) =>
        for {
          validBoardCards <- Validators.validateHandCards(cards.headOption.getOrElse(""), 10)
          validCards      <- cards.tail.map(cards => Validators.validateHandCards(cards, 8)).sequence
        } yield OmahaHoldem(Hand(validBoardCards), makeHands(validCards))

      case Some(FIVE_CARD_DRAW) =>
        cards
          .map(cards => Validators.validateHandCards(cards, 10))
          .sequence
          .map(makeHands)
          .map(FiveCardDraw)

      case Some(_) =>
        Left(SpawnPokerGameError(s"Poker Game Type Not Match with $TEXAS_HOLDEM or $OMAHA_HOLDEM or $FIVE_CARD_DRAW"))

      case None =>
        Left(SpawnPokerGameError("Poker Game Type Empty"))

    }

}

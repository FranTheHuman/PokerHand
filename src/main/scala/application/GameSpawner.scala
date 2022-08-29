package application

import application.Validators.Validation
import application.models.errors.SpawnPokerGameError
import cats.implicits.toTraverseOps
import domain.models.Hand.makeHands
import domain.models.PokerGame.{FiveCardDraw, OmahaHoldem, TexasHoldem}
import domain.models.names.PokerGamesNames
import domain.models.{Hand, PokerGame}

trait GameSpawner {

  def spawn: String => Validation[PokerGame]

}

object GameSpawner extends GameSpawner with PokerGamesNames {

  override def spawn: String => Validation[PokerGame] =
    str =>
      for {
        inputs <- Validators.validateInputLength(str)
        params <- Validators.validateParametersLength(inputs)
        game   <- createByType(typeGame = params.headOption, params.tail)
      } yield game

  private def createByType(typeGame: Option[String], cards: List[String]): Validation[PokerGame] =
    typeGame match {

      case Some(TEXAS_HOLDEM) =>
        for {
          validBoardCards      <- Validators.validateHandCards(cards.headOption.getOrElse(""), 10)
          validCards           <- Validators.validateInputConsistency(cards.tail)
          validCardsForTheGame <- validCards.tail.map(c => Validators.validateHandCards(c, 4)).sequence
        } yield TexasHoldem(Hand(validBoardCards), makeHands(validCardsForTheGame))

      case Some(OMAHA_HOLDEM) =>
        for {
          validBoardCards      <- Validators.validateHandCards(cards.headOption.getOrElse(""), 10)
          validCards           <- Validators.validateInputConsistency(cards.tail)
          validCardsForTheGame <- validCards.tail.map(c => Validators.validateHandCards(c, 8)).sequence
        } yield OmahaHoldem(Hand(validBoardCards), makeHands(validCardsForTheGame))

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

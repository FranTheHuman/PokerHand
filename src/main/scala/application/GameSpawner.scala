package application

import application.models.errors.SpawnPokerGameError
import cats.implicits._
import domain.models.Hand.makeHands
import domain.models.PokerGame.{FiveCardDraw, OmahaHoldem, TexasHoldem}
import domain.models.names.PokerGamesNames
import domain.models.{Hand, PokerGame}
import globals.Validation

trait GameSpawner[F[_]] {

  def spawn: String => F[PokerGame]

}

object GameSpawner extends GameSpawner[Validation] with PokerGamesNames {

  import globals.Validation._

  override def spawn: String => Validation[PokerGame] =
    str =>
      for {
        inputs <- Validations.validateInputLength(str)
        params <- Validations.validateParametersLength(inputs)
        game   <- createByType(typeGame = params.headOption, params.tail)
      } yield game

  private def createByType(typeGame: Option[String], cards: List[String]): Validation[PokerGame] =
    typeGame match {

      case Some(TEXAS_HOLDEM) =>
        for {
          validBoardCards      <- Validations.validateHandCards(cards.headOption.getOrElse(""), 10)
          validCards           <- Validations.validateInputConsistency(cards.tail)
          validCardsForTheGame <- validCards.map(c => Validations.validateHandCards(c, 4)).sequence
        } yield TexasHoldem(Hand(validBoardCards), makeHands(validCardsForTheGame))

      case Some(OMAHA_HOLDEM) =>
        for {
          validBoardCards      <- Validations.validateHandCards(cards.headOption.getOrElse(""), 10)
          validCards           <- Validations.validateInputConsistency(cards.tail)
          validCardsForTheGame <- validCards.map(c => Validations.validateHandCards(c, 8)).sequence
        } yield OmahaHoldem(Hand(validBoardCards), makeHands(validCardsForTheGame))

      case Some(FIVE_CARD_DRAW) =>
        cards
          .map(cards => Validations.validateHandCards(cards, 10))
          .sequence
          .map(makeHands)
          .map(FiveCardDraw)

      case Some(_) =>
        left(
          SpawnPokerGameError(s"Poker Game Type Not Match with $TEXAS_HOLDEM or $OMAHA_HOLDEM or $FIVE_CARD_DRAW")
        )

      case None =>
        left(SpawnPokerGameError("Poker Game Type Empty"))

    }

}

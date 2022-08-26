package application.models

import application.errors.SpawnPokerGameError
import application.models.Card.toCards

sealed trait PokerGame

object PokerGame {

  final case class TexasHoldem(boardCards: Hand, hands: List[Hand]) extends PokerGame
  final case class OmahaHoldem(boardCards: Hand, hands: List[Hand]) extends PokerGame
  final case class FiveCardDraw(hands: List[Hand])                  extends PokerGame

  val withBoardCards: List[String] => Either[SpawnPokerGameError, PokerGame] =
    strings =>
      strings.headOption
        .map(toCards)
        .map(Hand)
        .map(boardCards => TexasHoldem(boardCards, strings.map(toCards).map(Hand)))
        .toRight(SpawnPokerGameError(s"Missing The Cards"))

}

package application.models

sealed trait PokerGame

object PokerGame {

  final case class TexasHoldem(boardCards: Hand, hands: List[Hand]) extends PokerGame
  final case class OmahaHoldem(boardCards: Hand, hands: List[Hand]) extends PokerGame
  final case class FiveCardDraw(hands: List[Hand])                  extends PokerGame

}

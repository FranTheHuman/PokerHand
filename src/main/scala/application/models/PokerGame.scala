package application.models

import application.Validators
import application.Validators.Validation
import application.models.Hand.makeHands
import cats.implicits.toTraverseOps
import cats.instances.either._

sealed trait PokerGame

object PokerGame {

  final case class TexasHoldem(boardCards: Hand, hands: List[Hand]) extends PokerGame
  final case class OmahaHoldem(boardCards: Hand, hands: List[Hand]) extends PokerGame
  final case class FiveCardDraw(hands: List[Hand])                  extends PokerGame

}

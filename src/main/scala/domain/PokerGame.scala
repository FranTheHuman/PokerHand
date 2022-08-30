package domain.models

import domain.models.Hand.hasSamePower

import scala.annotation.tailrec

sealed trait PokerGame {
  def play: String
}

object PokerGame {

  final case class TexasHoldem(boardCards: Hand, hands: List[Hand]) extends PokerGame {
    override def play: String = ???
  }

  final case class OmahaHoldem(boardCards: Hand, hands: List[Hand]) extends PokerGame {
    override def play: String = ???
  }

  final case class FiveCardDraw(hands: List[Hand]) extends PokerGame {

    override def play: String = {
      @tailrec
      def evalRec(hs: List[Hand], last: Option[Hand], result: String): String = hs match {
        case ::(head, next) =>
          last match {

            case Some(hand) if hasSamePower(hand, head) =>
              evalRec(next, Some(hand), result + s"=${Hand.asString(head)} ")

            case Some(hand) =>
              evalRec(next, Some(hand), result + s" ${Hand.asString(head)}")

            case None =>
              evalRec(next, Some(head), result + s"${Hand.asString(head)}")

          }
        case Nil => result
      }
      evalRec(hands.sorted, None, "")
    }

  }

  val asString: PokerGame => String = {
    case FiveCardDraw(hands)            => hands.map(Hand.asString).mkString(" ")
    case OmahaHoldem(boardCards, hands) => ""
    case TexasHoldem(boardCards, hands) => ""
  }

}

package domain.models

import domain.models.Hand.HandImplicits.handOrder
import domain.models.Hand.hasSamePower

import scala.annotation.tailrec

sealed trait PokerGame {

  def play: String
  protected def combine: (Hand, Hand) => Hand

  @tailrec
  final protected def resultRec(hs: List[Hand], last: Option[Hand], result: String): String = hs match {
    case ::(head, next) =>
      last match {

        case Some(last_hand) if hasSamePower(last_hand, head) =>
          resultRec(next, Some(head), result + s"=${Hand.asString(head)} ")

        case Some(_) =>
          resultRec(next, Some(head), result + s" ${Hand.asString(head)}")

        case None =>
          resultRec(next, Some(head), result + s"${Hand.asString(head)}")

      }
    case Nil => result
  }

}

object PokerGame {

  final case class TexasHoldem(boardCards: Hand, hands: List[Hand]) extends PokerGame {

    override protected def combine: (Hand, Hand) => Hand = (x1, x2) =>
      (x1.cards ++ x2.cards)
        .combinations(5)
        .toList
        .map(Hand.apply)
        .filter(h => !h.cards.containsSlice(x2.cards))
        .max

    override def play: String =
      resultRec(
        hands.map(h => Hand(h.cards, combine(h, boardCards).handType)).sorted,
        None,
        result = ""
      )

  }

  final case class OmahaHoldem(boardCards: Hand, hands: List[Hand]) extends PokerGame {

    override protected def combine: (Hand, Hand) => Hand = (x1, x2) =>
      (for {
        handCombinations  <- x1.cards.combinations(2).toList
        boardCombinations <- x2.cards.combinations(3).toList
        finalCombinations <- (handCombinations ++ boardCombinations).combinations(5).toList
        hand = Hand(finalCombinations)
      } yield hand).filter(h => !h.cards.containsSlice(x2.cards)).max

    override def play: String =
      resultRec(
        hands.map(h => Hand(h.cards, combine(h, boardCards).handType)).sorted,
        None,
        result = ""
      )
  }

  final case class FiveCardDraw(hands: List[Hand]) extends PokerGame {

    override protected def combine: (Hand, Hand) => Hand = (x1, _) => x1

    override def play: String =
      resultRec(hands.sorted, None, result = "")

  }

  val gameTypeString: PokerGame => String = {
    case _: FiveCardDraw => "FiveCardDraw"
    case _: OmahaHoldem  => "OmahaHoldem"
    case _: TexasHoldem  => "TexasHoldem"
  }

  val asString: PokerGame => String = {
    case FiveCardDraw(hands)   => hands map Hand.asString mkString " "
    case OmahaHoldem(_, hands) => hands map Hand.asString mkString " "
    case TexasHoldem(_, hands) => hands map Hand.asString mkString " "
  }

}

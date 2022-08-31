package domain.models

import cats.implicits.catsSyntaxSemigroup
import domain.models.Hand.HandImplicits.{comb, handOrder}
import domain.models.Hand.hasSamePower

import scala.annotation.tailrec

sealed trait PokerGame {
  def play: String

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
    override def play: String =
      resultRec(hands.map(h => h |+| boardCards).sorted, None, "")

  }

  final case class OmahaHoldem(boardCards: Hand, hands: List[Hand]) extends PokerGame {
    override def play: String = ???
  }

  final case class FiveCardDraw(hands: List[Hand]) extends PokerGame {

    override def play: String =
      resultRec(hands.sorted, None, "")

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

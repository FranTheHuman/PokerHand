package domain.models

import domain.HandType
import domain.HandType._
import domain.models.Card.{power, toCards}
import domain.models.Rank.getIndexPower

import scala.annotation.tailrec

/** Model that represents a set of Cards for a poker player
  *
  * @param cards
  *   list of cards
  */
case class Hand(cards: List[Card], handType: HandType) {

  val isSameSuit: Boolean =
    cards
      .groupBy(_.suit.value)
      .toList
      .length == 1

  val isConsecutive: Boolean = {
    val isNext: (Card, Card) => Boolean = (last, next) =>
      if (power(next) > power(last) && getIndexPower(next) - 1 == getIndexPower(last)) true
      else false

    @tailrec
    def rec(_cards: List[Card], lastValue: Card, result: Boolean): Boolean = _cards match {
      case ::(head, tail) if isNext(lastValue, head) => rec(tail, head, result = true)
      case ::(_, _)                                  => false
      case Nil                                       => result
    }

    rec(cards, Card.empty, result = false)
  }

}

object Hand {

  implicit val handOrder: Ordering[Hand] =
    Ordering.by(h =>
      (
        getPower(h.handType),               // ORDER BY TYPE HAND POWER
        h.handType.higherRank,              // ORDER BY HIGHEST RANK
        h.cards.map(_.rank).mkString.sorted // ORDER ALPHABETIC
      )
    )

  def apply(cards: List[Card]): Hand =
    Hand(cards, evalCards(cards))

  def apply(input: String): Hand =
    Hand(toCards(input))

  val empty: Hand =
    Hand(List.empty[Card])

  val asString: Hand => String =
    hand => s"${hand.cards.map(c => s"${c.rank.value}${c.suit.value}").mkString}"

  def group(cards: List[Card]): List[(Rank, Int)] =
    cards
      .map(_.rank)
      .groupBy(identity)
      .toList
      .map { case (rank, repeated) => (rank, repeated.size) }

  def hasSamePower(hand1: Hand, hand2: Hand): Boolean =
    if (
      HandType.getPower(hand1.handType) == HandType.getPower(hand2.handType) &&
      Rank.getPower(hand1.handType.higherRank.value) == Rank.getPower(hand2.handType.higherRank.value)
    ) true
    else false

  def makeHands(inputs: List[String]): List[Hand] =
    inputs map toCards map Hand.apply

}

package application.models

import application.models.Card.{power, toCards}
import domain.HandType
import domain.HandType._

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
      if (power(next) > power(last)) true // TODO: MORE VALIDATIONS
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

  def apply(cards: List[Card]): Hand = {
    val hand                     = Hand(cards, HighCard(Rank.empty))
    val groups                   = group(hand)
    val groupHas: Int => Boolean = n => groups map (_._2) contains n
    val higher: Rank             = groups.map(_._1).max
    if (hand.isSameSuit && hand.isConsecutive)
      hand.copy(handType = StraightFlush(higher))
    else if (groupHas(4))
      hand.copy(handType = FourOfAKind(higher))
    else if (groupHas(3) && groupHas(2))
      hand.copy(handType = FullHouse(higher))
    else if (hand.isSameSuit)
      hand.copy(handType = Flush(higher))
    else if (hand.isConsecutive)
      hand.copy(handType = Straight(higher))
    else if (groupHas(3))
      hand.copy(handType = ThreeOfAKind(higher))
    else if (groupHas(2) && groupHas(2))
      hand.copy(handType = TwoPair(higher))
    else if (groupHas(2))
      hand.copy(handType = Pair(higher))
    else hand.copy(handType = HighCard(higher))
  }

  def group(hand: Hand): List[(Rank, Int)] =
    hand.cards
      .map(_.rank)
      .groupBy(identity)
      .toList
      .map { case (rank, repeated) => (rank, repeated.size) }

  def apply(input: String): Hand =
    Hand(toCards(input))

  def sort(hand: Hand): Hand =
    hand copy (cards = hand.cards.sortWith(power(_) > power(_)))

  def makeHands(inputs: List[String]): List[Hand] =
    inputs map toCards map Hand.apply

  def empty: Hand =
    Hand(List.empty[Card])

}

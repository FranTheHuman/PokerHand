package application.models

import application.models.Card.{power, toCards}
import domain.HandType
import domain.HandType.HighCard

import scala.annotation.tailrec

/** Model that represents a set of Cards for a poker player
  *
  * @param cards
  *   list of cards
  */
case class Hand(cards: List[Card], handType: HandType)

object Hand {

  def apply(cards: List[Card]): Hand =
    Hand(cards, HighCard)

  def apply(input: String): Hand =
    Hand(toCards(input), HighCard)

  def sort(hand: Hand): Hand =
    hand copy (
      cards = hand.cards.sortWith(power(_) > power(_))
    )

  def isSameSuit(hand: Hand): Boolean =
    hand.cards
      .groupBy(_.suit.value)
      .toList
      .length == 1

  def isConsecutive(hand: Hand): Boolean = {
    val isNext: (Card, Card) => Boolean = (last, next) =>
      if (power(next) > power(last)) true // TODO: MORE VALIDATIONS
      else false

    @tailrec
    def rec(cards: List[Card], lastValue: Card, result: Boolean): Boolean = cards match {
      case ::(head, tail) if isNext(lastValue, head) => rec(tail, head, result = true)
      case ::(head, tail)                            => false
      case Nil                                       => result
    }

    rec(hand.cards, Card.empty, result = false)
  }

  def makeHands(inputs: List[String]): List[Hand] =
    inputs map toCards map Hand.apply

  def empty: Hand =
    Hand(List.empty[Card])

}

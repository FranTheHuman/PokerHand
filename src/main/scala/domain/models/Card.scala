package domain.models

import domain.models.Rank.getIndexPower

import scala.annotation.tailrec

/** Model that represents a simple poker card
  *
  * @param rank
  *   rank
  * @param suit
  *   suit
  */
case class Card(rank: Rank, suit: Suit)

object Card {

  val toCards: String => List[Card] =
    _.grouped(2)
      .map(_.toCharArray)
      .filter(_.length.equals(2))
      .map(couple => Card(Rank(couple.head), Suit(couple.last)))
      .toList

  val areSameSuit: List[Card] => Boolean =
    cards =>
      cards
        .groupBy(_.suit.value)
        .toList
        .length == 1

  val areConsecutive: List[Card] => Boolean =
    cards => {
      val isNext: (Card, Card) => Boolean = (last, next) =>
        if (power(next) > power(last) && getIndexPower(next) - 1 == getIndexPower(last)) true
        else false

      @tailrec
      def rec(_cards: List[Card], lastValue: Card, result: Boolean): Boolean = _cards match {
        case ::(head, tail) if isNext(lastValue, head) => rec(tail, head, result = true)
        case ::(_, _) => false
        case Nil => result
      }

      rec(cards, Card.empty, result = false)
    }

  val power: Card => Int =
    card =>
      Rank.powers
        .find(v => v._1 == card.rank.value)
        .getOrElse(Char.MinValue -> Int.MinValue)
        ._2

  def empty: Card =
    Card(Rank.empty, Suit.empty)

}

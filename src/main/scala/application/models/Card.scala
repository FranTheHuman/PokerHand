package application.models

import domain.RankValues

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

  def power(card: Card): Int =
    RankValues()
      .find(v => v._1 == card.rank.value)
      .getOrElse('0' -> 0)
      ._2

  def empty: Card =
    Card(Rank('0'), Suit('0'))

}

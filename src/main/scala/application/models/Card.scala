package application.models

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
    Rank.powers
      .find(v => v._1 == card.rank.value)
      .getOrElse(Char.MinValue -> Int.MinValue)
      ._2

  def empty: Card =
    Card(Rank.empty, Suit.empty)

}

package application.models

import application.models.Card.toCards

/** Model that represents a set of Cards for a poker player
  *
  * @param cards
  *   list of cards
  */
case class Hand(cards: List[Card]) extends AnyVal

object Hand {

  def apply(input: String): Hand =
    Hand(toCards(input))

  def makeHands(inputs: List[String]): List[Hand] =
    inputs map toCards map Hand.apply

  def empty: Hand =
    Hand(List.empty[Card])

}

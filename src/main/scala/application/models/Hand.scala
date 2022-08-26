package application.models

/** Model that represents a set of Cards for a poker player
  * @param cards
  *   list of cards
  */
case class Hand(cards: List[Card]) extends AnyVal
package domain

import application.models.Rank

sealed trait HandType {
  val higherRank: Rank
}

object HandType {

  case class StraightFlush(higherRank: Rank) extends HandType
  case class FourOfAKind(higherRank: Rank)   extends HandType
  case class FullHouse(higherRank: Rank)     extends HandType
  case class Flush(higherRank: Rank)         extends HandType
  case class Straight(higherRank: Rank)      extends HandType
  case class ThreeOfAKind(higherRank: Rank)  extends HandType
  case class TwoPair(higherRank: Rank)       extends HandType
  case class Pair(higherRank: Rank)          extends HandType
  case class HighCard(higherRank: Rank)      extends HandType

}

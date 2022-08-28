package domain

sealed trait HandType

object HandType {

  case object StraightFlush extends HandType
  case object FourOfAKind   extends HandType
  case object FullHouse     extends HandType
  case object Flush         extends HandType
  case object Straight      extends HandType
  case object ThreeOfAKind  extends HandType
  case object TwoPair       extends HandType
  case object Pair          extends HandType
  case object HighCard      extends HandType

}

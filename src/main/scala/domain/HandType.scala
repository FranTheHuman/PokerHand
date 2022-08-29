package domain

import domain.models.Card.{areConsecutive, areSameSuit}
import domain.models._

import scala.annotation.tailrec

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

  val evalCards: List[Card] => HandType =
    cards => {

      val groups: Seq[(Rank, Int)] = Hand.group(cards)
      val groupHas: Int => Boolean = n => groups map (_._2) contains n
      val higher: Rank             = findHigherRankRec(groups, 5)

      cards match {
        case c if areSameSuit(c) && areConsecutive(c)                  => StraightFlush(higher)
        case c if groupHas(4)                                          => FourOfAKind(higher)
        case c if groupHas(3) && groupHas(2)                           => FullHouse(higher)
        case c if areSameSuit(c)                                       => Flush(higher)
        case c if areConsecutive(c)                                    => Straight(higher)
        case c if groupHas(3)                                          => ThreeOfAKind(higher)
        case c if groupHas(2) && groups.filterNot(_._2 == 2).size == 1 => TwoPair(higher)
        case c if groupHas(2)                                          => Pair(higher)
        case _                                                         => HighCard(higher)
      }

    }

  val getPower: HandType => Int =
    t => powers getOrElse (asString(t), 0)

  @tailrec
  private def findHigherRankRec(groups: Seq[(Rank, Int)], n: Int): Rank =
    if (n <= 1) groups.map(_._1).headOption.getOrElse(Rank.empty)
    else if (groups.exists(_._2 == n)) groups.filter(_._2 == n).map(_._1).max
    else findHigherRankRec(groups, n - 1)

  private def powers: Map[String, Int] = Map(
    "HighCard"      -> 1,
    "Pair"          -> 2,
    "TwoPair"       -> 3,
    "ThreeOfAKind"  -> 4,
    "Straight"      -> 5,
    "Flush"         -> 6,
    "FullHouse"     -> 7,
    "FourOfAKind"   -> 8,
    "StraightFlush" -> 9
  )

  private val asString: HandType => String = {
    case _: StraightFlush => "StraightFlush"
    case _: FourOfAKind   => "FourOfAKind"
    case _: FullHouse     => "FullHouse"
    case _: Flush         => "Flush"
    case _: Straight      => "Straight"
    case _: ThreeOfAKind  => "ThreeOfAKind"
    case _: TwoPair       => "TwoPair"
    case _: Pair          => "Pair"
    case _: HighCard      => "HighCard"
  }

}

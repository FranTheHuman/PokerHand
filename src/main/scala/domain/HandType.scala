package domain

import domain.models.Card.{areConsecutive, areSameSuit}
import domain.models._

import scala.annotation.tailrec

sealed trait HandType {

  val higherRank: Rank
  val remainingHigherRank: Rank

  def setHigherRank(ht: HandType, r1: Rank, r2: Rank): HandType = ht match {
    case _: HandType.StraightFlush => HandType.StraightFlush(r1, r2)
    case _: HandType.FourOfAKind   => HandType.FourOfAKind(r1, r2)
    case _: HandType.FullHouse     => HandType.FullHouse(r1, r2)
    case _: HandType.Flush         => HandType.Flush(r1, r2)
    case _: HandType.Straight      => HandType.Straight(r1, r2)
    case _: HandType.ThreeOfAKind  => HandType.ThreeOfAKind(r1, r2)
    case _: HandType.TwoPair       => HandType.TwoPair(r1, r2)
    case _: HandType.Pair          => HandType.Pair(r1, r2)
    case _: HandType.HighCard      => HandType.HighCard(r1, r2)
  }

}

object HandType {

  case class StraightFlush(higherRank: Rank, remainingHigherRank: Rank) extends HandType
  case class FourOfAKind(higherRank: Rank, remainingHigherRank: Rank)   extends HandType
  case class FullHouse(higherRank: Rank, remainingHigherRank: Rank)     extends HandType
  case class Flush(higherRank: Rank, remainingHigherRank: Rank)         extends HandType
  case class Straight(higherRank: Rank, remainingHigherRank: Rank)      extends HandType
  case class ThreeOfAKind(higherRank: Rank, remainingHigherRank: Rank)  extends HandType
  case class TwoPair(higherRank: Rank, remainingHigherRank: Rank)       extends HandType
  case class Pair(higherRank: Rank, remainingHigherRank: Rank)          extends HandType
  case class HighCard(higherRank: Rank, remainingHigherRank: Rank)      extends HandType

  val evalCards: List[Card] => HandType =
    cards => {

      val groups: Seq[(Rank, Int)]   = Hand.group(cards)
      val hasGroupOf: Int => Boolean = n => groups map (_._2) contains n
      val highers: (Rank, Rank)      = findHigherRankRec(groups, 5)

      cards match {
        case c if areSameSuit(c) && areConsecutive(c)                    => StraightFlush(highers._1, highers._2)
        case c if hasGroupOf(4)                                          => FourOfAKind(highers._1, highers._2)
        case c if hasGroupOf(3) && hasGroupOf(2)                         => FullHouse(highers._1, highers._2)
        case c if areSameSuit(c)                                         => Flush(highers._1, highers._2)
        case c if areConsecutive(c)                                      => Straight(highers._1, highers._2)
        case c if hasGroupOf(3)                                          => ThreeOfAKind(highers._1, highers._2)
        case c if hasGroupOf(2) && groups.filterNot(_._2 == 2).size == 1 => TwoPair(highers._1, highers._2)
        case c if hasGroupOf(2)                                          => Pair(highers._1, highers._2)
        case _                                                           => HighCard(highers._1, highers._2)
      }
    }

  val getPower: HandType => Int =
    t => powers getOrElse (asString(t), 0)

  val determineHighestAndHighestRemaining: (Int, Seq[(Rank, Int)]) => (Rank, Rank) = (n, g) => {
    val first        = g.filter(_._2 == n).map(_._1).max
    val withoutFirst = g.filterNot(_._1.equals(first))
    if (withoutFirst.nonEmpty) (first, withoutFirst.map(_._1).max)
    else (first, Rank.empty)
  }

  @tailrec
  private def findHigherRankRec(groups: Seq[(Rank, Int)], n: Int): (Rank, Rank) =
    if (n <= 1 && groups.nonEmpty)
      determineHighestAndHighestRemaining(1, groups)
    else if (n <= 1 && groups.isEmpty)
      (Rank.empty, Rank.empty)
    else if (groups.exists(_._2 == n))
      determineHighestAndHighestRemaining(n, groups)
    else findHigherRankRec(groups, n - 1)

  private val powers: Map[String, Int] = Map(
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

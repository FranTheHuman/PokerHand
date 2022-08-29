package domain.models

/** Class that represents a card rank
  * @param value
  *   one of `h`, `d`, `c`, `s`
  */
case class Rank(value: Char) extends AnyVal

object Rank {

  implicit val rankOrder: Ordering[Rank] =
    Ordering.by(r => getPower(r.value))

  val getPower: Char => Int =
    c => powers getOrElse (c, 0)

  def powers: Map[Char, Int] = Map(
    '2' -> 1,
    '3' -> 2,
    '4' -> 3,
    '5' -> 4,
    '6' -> 5,
    '7' -> 6,
    '8' -> 7,
    '9' -> 8,
    'T' -> 9,
    'J' -> 10,
    'Q' -> 11,
    'K' -> 12,
    'A' -> 13
  )

  val getIndexPower: Card => Int =
    c => Rank.powers.keysIterator.toList.indexOf(c.rank.value)

  def empty: Rank =
    Rank(Char.MinValue)

}

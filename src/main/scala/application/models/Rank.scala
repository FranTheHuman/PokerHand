package application.models

/** Class that represents a card rank
  * @param value
  *   one of `h`, `d`, `c`, `s`
  */
case class Rank(value: Char) extends AnyVal

object Rank {

  implicit val maxRank: Ordering[Rank] = new Ordering[Rank] {
    override def compare(x: Rank, y: Rank): Int = x.value compare y.value
  }

  def powers: Map[Char, Int] = Map(
    '2' -> 2,
    '3' -> 3,
    '4' -> 4,
    '5' -> 5,
    '6' -> 6,
    '7' -> 7,
    '8' -> 8,
    '9' -> 9,
    'T' -> 10,
    'J' -> 11,
    'Q' -> 12,
    'K' -> 13,
    'A' -> 14
  )

  def empty: Rank =
    Rank(Char.MinValue)

}

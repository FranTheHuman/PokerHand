package domain.models

/** Class that represents a suit
  * @param value
  *   one of `A`, `K`, `Q`, `J`, `T`, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`
  */
case class Suit(value: Char) extends AnyVal

object Suit {

  def empty: Suit =
    Suit(Char.MinValue)

}

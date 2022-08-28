package domain

import application.models.{Hand, PokerGame}

trait StrengthPokerEval {

  def eval: PokerGame => String

}

object StrengthPokerEval extends StrengthPokerEval {

  override def eval: PokerGame => String = {
    case PokerGame.TexasHoldem(boardCards, hands) => ???
    case PokerGame.OmahaHoldem(boardCards, hands) => ???
    case PokerGame.FiveCardDraw(hands) =>
      hands map Hand.sort sortWith { (first, next) => ??? } mkString "\n"
  }

}

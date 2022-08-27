package domain

import application.models.PokerGame

trait StrengthPokerEval {

  def eval: PokerGame => String

}

object StrengthPokerEval extends StrengthPokerEval {

  override def eval: PokerGame => String = game => game.toString

}

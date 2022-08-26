package domain

import application.models.PokerGame

trait StrengthPokerEval {

  def play: PokerGame => String

}

object StrengthPokerEval extends StrengthPokerEval {

  override def play: PokerGame => String = game => game.toString

}

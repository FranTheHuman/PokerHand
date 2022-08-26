package domain

import application.models.PokerGame

trait PlayPoker {

  def play: PokerGame => String

}

object PlayPoker extends PlayPoker {

  override def play: PokerGame => String = ???

}

package application.runners

import application.GameSpawner
import cats.effect.IO
import domain.PlayPoker

object GameRunnerFromCommand extends GameRunner[IO] {
  override def run: IO[Unit] = for {
    input              <- IO.readLine
    validatedPokerGame <- IO.fromEither(GameSpawner.spawn(input))
    result             <- IO(PlayPoker.play(validatedPokerGame))
    _                  <- IO println result
  } yield ()
}

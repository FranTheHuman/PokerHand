package application.runners

import cats.effect.IO

object GameRunnerFromFile extends GameRunner[IO] {
  override def run: IO[Unit] = ???
}

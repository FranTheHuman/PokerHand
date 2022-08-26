package application.runners

import cats.effect.{ExitCode, IO}

object GameRunnerFromFile extends GameRunner[IO] {
  override def run: IO[ExitCode] = ???
}

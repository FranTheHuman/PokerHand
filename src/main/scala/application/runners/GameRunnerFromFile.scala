package application.runners

import cats.effect.{ExitCode, IO}

object GameRunnerFromFile extends GameRunner[IO, ExitCode] {
  override def run: IO[ExitCode] = ???
}

package application.runners

import cats.effect.{ExitCode, IO}

object GameRunnerFromGraphQl extends GameRunner[IO, ExitCode] {
  override def run: IO[ExitCode] = ???
}

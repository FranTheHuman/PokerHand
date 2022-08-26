package application.runners

import cats.effect.{ExitCode, IO}

object GameRunnerFromGraphQl extends GameRunner[IO] {
  override def run: IO[ExitCode] = ???
}

package application.runners

import cats.effect.IO

object GameRunnerFromGraphQl extends GameRunner[IO] {
  override def run: IO[Unit] = ???
}

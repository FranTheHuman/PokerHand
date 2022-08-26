package application.runners

import application.Configurations.Configurations.SERVER_CONFIG
import cats.effect.{ExitCode, IO}
import infrastructure.adapter.in.rest.GameServer

object GameRunnerFromRestApi extends GameRunner[IO] {
  override def run: IO[Unit] =
    GameServer boostrap [IO] SERVER_CONFIG use (_ => IO.never) as ExitCode.Success
}

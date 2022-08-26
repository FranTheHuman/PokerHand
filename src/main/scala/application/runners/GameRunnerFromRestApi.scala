package application.runners

import application.Configurations.Configurations.SERVER_CONFIG
import cats.effect.{ExitCode, IO}
import infrastructure.adapter.in.rest.GameServer
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object GameRunnerFromRestApi extends GameRunner[IO] {

  implicit val logger: Logger[IO] =
    Slf4jLogger.getLogger[IO]

  override def run: IO[ExitCode] =
    GameServer
      .boostrap[IO](SERVER_CONFIG)
      .use(_ => IO.never)
      .as(ExitCode.Success)
}

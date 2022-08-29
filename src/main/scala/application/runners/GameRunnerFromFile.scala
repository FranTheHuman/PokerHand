package application.runners

import application.GameSpawner
import cats.effect.{ExitCode, IO}
import cats.implicits.toTraverseOps
import infrastructure.adapter.in.file.FileReader
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import application.models.Configurations.Configurations.FILE_PATH

object GameRunnerFromFile extends GameRunner[IO, ExitCode] {

  implicit val logger: Logger[IO] =
    Slf4jLogger.getLogger[IO]

  override def run: IO[ExitCode] =
    FileReader read [IO] FILE_PATH use { inputs =>
      for {
        _                   <- Logger[IO] info s"INPUTS: ${inputs mkString "\n"}"
        validatedPokerGames <- (inputs map GameSpawner.spawn map IO.fromEither).sequence
        _                   <- Logger[IO] info s"GAMES: ${validatedPokerGames mkString "\n"}"
        result              <- IO(validatedPokerGames.map(_.eval))
        _                   <- Logger[IO] info s"RESULT: ${result mkString "\n"}"
      } yield ExitCode.Success
    }
}

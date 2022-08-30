package application.runners

import application.GameSpawner
import application.models.Configurations.Configurations.FILE_PATH
import application.models.errors.SpawnPokerGameError
import cats.effect.{ExitCode, IO}
import cats.implicits.toTraverseOps
import domain.models.PokerGame
import infrastructure.adapter.in.file.FileReader
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object GameRunnerFromFile extends GameRunner[IO, ExitCode] {

  implicit val logger: Logger[IO] =
    Slf4jLogger.getLogger[IO]

  override def run: IO[ExitCode] =
    FileReader read [IO] FILE_PATH use { inputs =>
      for {
        _                   <- Logger[IO] info s"INPUTS: ${inputs mkString "\n"}"
        validatedPokerGames <- mapAndSpawn(inputs)
        _                   <- Logger[IO] info s"GAMES: ${validatedPokerGames mkString "\n"}"
        result              <- IO(validatedPokerGames.map(_.eval))
        _                   <- Logger[IO] info s"RESULT: ${result mkString "\n"}"
      } yield ExitCode.Success
    }

  val mapAndSpawn: List[String] => IO[List[PokerGame]] =
    inputs =>
      inputs.map { input =>
        GameSpawner
          .spawn(input)
          .fold(
            e => IO.raiseError(SpawnPokerGameError(e.toString.mkString("\n"))),
            IO.pure
          )
      }.sequence
}

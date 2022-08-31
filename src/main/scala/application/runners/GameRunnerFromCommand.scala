package application.runners

import application.models.errors.SpawnPokerGameError
import application.{ErrorHandler, GameSpawner, GamesRepository}
import cats.effect.{ExitCode, IO}
import domain.models.PokerGame.asString
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object GameRunnerFromCommand extends GameRunner[IO, ExitCode] {

  implicit val logger: Logger[IO] =
    Slf4jLogger.getLogger[IO]

  override def run: IO[ExitCode] = (for {
    input <- IO.readLine
    _     <- Logger[IO] info s"INPUT: $input"

    validatedPokerGame <- GameSpawner spawn input fold (e => IO raiseError e, IO.pure)
    _                  <- Logger[IO] info s"GAME: ${asString(validatedPokerGame)}"
    result             <- IO(validatedPokerGame.play)
    _                  <- GamesRepository.persistGame[IO](validatedPokerGame, result)
    _                  <- Logger[IO] info s"RESULT: $result"
  } yield ExitCode.Success) handleErrorWith (ErrorHandler[IO, ExitCode](_, ExitCode.Error))
}

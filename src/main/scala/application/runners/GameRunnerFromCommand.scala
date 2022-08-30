package application.runners

import application.GameSpawner
import application.models.errors.SpawnPokerGameError
import cats.effect.{ExitCode, IO}
import domain.models.PokerGame.asString
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object GameRunnerFromCommand extends GameRunner[IO, ExitCode] {

  implicit val logger: Logger[IO] =
    Slf4jLogger.getLogger[IO]

  override def run: IO[ExitCode] = (for {
    input              <- IO.readLine
    _                  <- Logger[IO] info s"INPUT: $input"
    validatedPokerGame <- GameSpawner.spawn(input).fold(e => IO.raiseError(SpawnPokerGameError(e.toString)), IO.pure)
    _                  <- Logger[IO] info s"GAME: ${asString(validatedPokerGame)}"
    result             <- IO(validatedPokerGame.eval)
    _                  <- Logger[IO] info s"RESULT: $result"
  } yield ExitCode.Success) handleErrorWith {
    case error: SpawnPokerGameError =>
      Logger[IO].error(s"ERROR: $error : ${error.message}") >> IO(ExitCode.Error)
    case error =>
      Logger[IO].error(s"ERROR: $error") >> IO(ExitCode.Error)
  }
}

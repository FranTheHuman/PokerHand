package application.runners

import application.GameSpawner
import cats.effect.{ExitCode, IO}
import domain.StrengthPokerEval
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object GameRunnerFromCommand extends GameRunner[IO] {

  implicit val logger: Logger[IO] =
    Slf4jLogger.getLogger[IO]

  override def run: IO[ExitCode] = for {
    input              <- IO.readLine
    _                  <- Logger[IO] info s"INPUT: $input"
    validatedPokerGame <- IO.fromEither(GameSpawner.spawn(input))
    _                  <- Logger[IO] info s"GAME: $validatedPokerGame"
    result             <- IO(StrengthPokerEval.play(validatedPokerGame))
    _                  <- Logger[IO] info s"RESULT: $result"
  } yield ExitCode.Success
}

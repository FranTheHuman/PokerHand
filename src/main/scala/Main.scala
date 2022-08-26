import application.runners.GameRunnerFromCommand
import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val run: IO[Unit] = GameRunnerFromCommand.run
}

import application.runners.{GameRunnerFromCommand, GameRunnerFromFile, GameRunnerFromGraphQl, GameRunnerFromRestApi}
import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    args.headOption match {
      case Some("command-line") => GameRunnerFromCommand.run
      case Some("rest")         => GameRunnerFromRestApi.run
      case Some("file")         => GameRunnerFromFile.run
      case Some("graphql")      => GameRunnerFromGraphQl.run
      case _ =>
        IO
          .pure(System.err.println("Usage: sbt 'run command-line | rest | file | graphql' "))
          .as(ExitCode(2))
    }
}

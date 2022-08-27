import application.models.names.GameRunnersNames
import application.runners.{GameRunnerFromCommand, GameRunnerFromFile, GameRunnerFromGraphQl, GameRunnerFromRestApi}
import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp with GameRunnersNames {
  def run(args: List[String]): IO[ExitCode] =
    args.headOption match {
      case Some(COMMAND_LINE) => GameRunnerFromCommand.run
      case Some(REST)         => GameRunnerFromRestApi.run
      case Some(FILE)         => GameRunnerFromFile.run
      case Some(GRAPHQL)      => GameRunnerFromGraphQl.run
      case _ =>
        IO
          .pure(System.err.println(s"Usage: sbt 'run $COMMAND_LINE | $REST | $FILE | $GRAPHQL' "))
          .as(ExitCode(2))
    }
}

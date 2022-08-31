package application

import application.models.errors.SpawnPokerGameError
import cats.Monad
import cats.implicits.catsSyntaxFlatMapOps
import org.typelevel.log4cats.Logger

object ErrorHandler {

  def apply[F[_]: Monad: Logger, T](t: Throwable, result: T): F[T] = t match {
    case error: SpawnPokerGameError =>
      Logger[F].error(s"ERROR: $error : ${error.message}") >> Monad[F].pure(result)
    case error =>
      Logger[F].error(s"ERROR: ${error.getMessage}") >> Monad[F].pure(result)
  }

}

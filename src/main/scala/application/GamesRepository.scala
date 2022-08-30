package application

import application.models.Configurations.Configurations.DATABASE_CONFIG
import application.models.errors.PersistenceError
import cats.Applicative
import cats.effect.{Async, MonadCancel}
import cats.implicits.toTraverseOps
import domain.models.PokerGame
import doobie.implicits.toSqlInterpolator
import infrastructure.adapter.out.Database

import java.util.Date
import scala.concurrent.ExecutionContext.Implicits._

trait GamesRepository {
  def persistGame[F[_]](games: PokerGame, results: String)(implicit
      m: MonadCancel[F, Throwable],
      a: Async[F]
  ): F[Unit]
  def persistGames[F[_]: Applicative](games: List[PokerGame], results: List[String])(implicit
      m: MonadCancel[F, Throwable],
      a: Async[F]
  ): F[List[Unit]]
}

object GamesRepository extends GamesRepository {

  private val insertQuery: (String, String, Date) => doobie.Update0 =
    (a, b, c) => sql"insert into games (poker_type, result, played_at) values ($a, $b, $c)".update

  def persistGame[F[_]](games: PokerGame, results: String)(implicit
      m: MonadCancel[F, Throwable],
      a: Async[F]
  ): F[Unit] =
    Database
      .connect[F](DATABASE_CONFIG)
      .use { transactor =>
        Database insert (insertQuery(PokerGame.gameTypeString(games), results, new Date), transactor)
      }

  def persistGames[F[_]: Applicative](
      games: List[PokerGame],
      results: List[String]
  )(implicit m: MonadCancel[F, Throwable], a: Async[F]): F[List[Unit]] =
    Database
      .connect[F](DATABASE_CONFIG)
      .use { transactor =>
        games match {
          case l if l.size == results.size =>
            games
              .map(PokerGame.gameTypeString)
              .zip(results)
              .map(r => Database.insert(insertQuery(r._1, r._2, new Date), transactor))
              .sequence
          case _ =>
            MonadCancel[F].raiseError(PersistenceError("Inconsistency at persist games between results and game types"))
        }
      }

}

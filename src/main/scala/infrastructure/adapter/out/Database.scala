package infrastructure.adapter.out

import application.models.Configurations.DbConfig
import cats.effect.MonadCancel
import cats.effect.kernel.{Async, Resource}
import cats.implicits._
import doobie.hikari.HikariTransactor
import doobie.implicits._

import scala.concurrent.ExecutionContext

trait Database {

  def connect[F[_]](dbConfig: DbConfig)(implicit ec: ExecutionContext, a: Async[F]): Resource[F, HikariTransactor[F]] =
    HikariTransactor.newHikariTransactor[F](
      dbConfig.driver,
      dbConfig.url,
      dbConfig.user,
      dbConfig.pass,
      ec
    )

  def insert[F[_]](insertQuery: => doobie.Update0, h: HikariTransactor[F])(implicit
      m: MonadCancel[F, Throwable]
  ): F[Unit]
}

object Database extends Database {

  override def insert[F[_]](
      insertQuery: => doobie.Update0,
      h: HikariTransactor[F]
  )(implicit m: MonadCancel[F, Throwable]): F[Unit] =
    insertQuery.run
      .transact(h)
      .map(_ => ())

}

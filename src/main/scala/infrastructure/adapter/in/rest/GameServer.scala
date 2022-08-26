package infrastructure.adapter.in.rest

import application.Configurations.HttpConfig
import application.Configurations.HttpConfig.{getHost, getPath, getPort}
import cats.effect.Resource
import cats.effect.kernel.{Async, Concurrent}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.typelevel.log4cats.Logger

trait GameServer {

  def boostrap[F[_]: Concurrent: Async: Logger](conf: HttpConfig): Resource[F, Server]

}

object GameServer extends GameServer {

  def boostrap[F[_]: Concurrent: Async: Logger](conf: HttpConfig): Resource[F, Server] =
    EmberServerBuilder
      .default[F]
      .withHost(getHost(conf))
      .withPort(getPort(conf))
      .withHttpApp(Router.routes[F](getPath(conf)).orNotFound)
      .build

}

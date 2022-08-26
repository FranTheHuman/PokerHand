package infrastructure.adapter.in.rest

import application.Configurations.HttpConfig
import cats.effect.Resource
import cats.effect.kernel.{Async, Concurrent}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

trait GameServer {

  def boostrap[F[_]: Concurrent: Async](conf: HttpConfig): Resource[F, Server]

}

object GameServer extends GameServer {

  def boostrap[F[_]: Concurrent: Async](conf: HttpConfig): Resource[F, Server] =
    EmberServerBuilder
      .default[F]
      .withHost(conf.host)
      .withPort(conf.port)
      .withHttpApp(Router.routes[F](conf.pathName).orNotFound)
      .build

}

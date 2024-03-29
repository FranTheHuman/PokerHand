package infrastructure.adapter.in.rest

import application.models.errors.SpawnPokerGameError
import application.{GameSpawner, GamesRepository}
import cats.effect.Async
import cats.effect.kernel.Concurrent
import cats.syntax.all._
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.dsl.{Http4sDsl, RequestDslBinCompat}
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes, Uri}
import org.typelevel.log4cats.Logger

trait Router {
  def routes[F[_]: Concurrent: Async: Logger](path: Uri.Path): HttpRoutes[F]
}

object Router extends Router {

  import globals.Validation._

  implicit def decoder[F[_]: Concurrent]: EntityDecoder[F, List[String]] = jsonOf[F, List[String]]
  implicit def encoder[F[_]: Concurrent]: EntityEncoder[F, Json]         = jsonEncoderOf[F, Json]

  override def routes[F[_]: Concurrent: Async: Logger](PathName: Uri.Path): HttpRoutes[F] = {
    val dsl: Http4sDsl[F] with RequestDslBinCompat = Http4sDsl[F]
    import dsl._
    HttpRoutes
      .of[F] {
        case req @ POST -> PathName =>
          (for {
            inputs              <- req.as[List[String]]
            _                   <- Logger[F] info s"INPUTS: $inputs"
            validatedPokerGames <- Concurrent[F] fromValidated inputs.map(GameSpawner.spawn).sequence
            _                   <- Logger[F] info s"VALID GAMES: $validatedPokerGames"
            result              <- Concurrent[F] pure validatedPokerGames.map(_.play)
            _                   <- GamesRepository persistGames [F] (validatedPokerGames, result)
            _                   <- Logger[F] info s"RESULTS: $result"
            response            <- Ok(result.asJson)
          } yield response) handleErrorWith {
            case error: SpawnPokerGameError => BadRequest(s"${error.message}")
            case error: Throwable           => InternalServerError(error.getMessage)
          }
        case _ => NotFound("Not the path Bro")
      }
  }

}

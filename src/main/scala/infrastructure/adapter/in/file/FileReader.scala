package infrastructure.adapter.in.file

import application.Configurations.FilePath
import cats.Monad
import cats.effect.Resource
import cats.syntax.all._
import org.typelevel.log4cats.Logger

import scala.io.Source

trait FileReader[T] {
  def read[F[_]: Monad: Logger](filePath: FilePath): Resource[F, T]
}

object FileReader extends FileReader[List[String]] {

  override def read[F[_]: Monad: Logger](filePath: FilePath): Resource[F, List[String]] =
    Resource
      .make(Monad[F] pure Source.fromResource(filePath.path)) { s =>
        Logger[F].info("Closing the file reader") >> Monad[F].pure(s.close())
      } map (_.getLines().toList)

}

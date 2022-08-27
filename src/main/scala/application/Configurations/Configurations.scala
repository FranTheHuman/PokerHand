package application.Configurations

import pureconfig.ConfigSource
import pureconfig.generic.auto._

object Configurations {

  implicit lazy val SERVER_CONFIG: HttpConfig =
    ConfigSource.default
      .at("application.server")
      .load[HttpConfig]
      .getOrElse(HttpConfig.empty())

  implicit lazy val FILE_PATH: FilePath =
    ConfigSource.default
      .at("application.file")
      .load[FilePath]
      .getOrElse(FilePath(""))

}

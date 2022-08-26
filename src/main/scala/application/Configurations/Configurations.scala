package application.Configurations

import pureconfig.ConfigSource
import pureconfig.generic.auto._

object Configurations {

  lazy val SERVER_CONFIG: HttpConfig =
    ConfigSource
      .file("application.conf")
      .at("application.server")
      .load[HttpConfig]
      .getOrElse(HttpConfig.empty())

}

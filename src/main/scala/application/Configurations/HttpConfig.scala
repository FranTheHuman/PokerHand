package application.Configurations

import pureconfig._
import pureconfig.generic.auto._
import com.comcast.ip4s.{Host, Port}
import org.http4s.Uri

case class HttpConfig(host: Host, port: Port, pathName: Uri.Path)

object HttpConfig {

  def empty(): HttpConfig =
    HttpConfig(
      Host.fromString("localhost").getOrElse("localhost"),
      Port.fromString("8080").getOrElse("8080"),
      Uri.Path.empty
    )

}

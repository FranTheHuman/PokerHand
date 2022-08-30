package application.models.Configurations

import com.comcast.ip4s.{Host, Port}
import org.http4s.Uri
import org.http4s.Uri.Path.Root

case class HttpConfig(host: String, port: Int, pathName: String)

object HttpConfig {

  def empty(): HttpConfig =
    HttpConfig("localhost", 8080, "")

  def getHost(conf: HttpConfig): Host =
    Host
      .fromString(conf.host)
      .fold(Host.fromString("localhost").get)(host => host)

  def getPort(conf: HttpConfig): Port =
    Port
      .fromInt(conf.port)
      .fold(Port.fromString("8080").get)(port => port)

  def getPath(conf: HttpConfig): Uri.Path =
    Root / conf.pathName
}

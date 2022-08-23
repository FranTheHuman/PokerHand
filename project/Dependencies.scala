import sbt._

object Dependencies {

  object V {
    val circe      = "0.14.2"
    val logback    = "1.2.11"
    val pureconfig = "0.17.1"
    val cats       = "3.3.14"
  }

  val circeCore      = "io.circe"              %% "circe-core"      % V.circe
  val circeGenerics  = "io.circe"              %% "circe-generic"   % V.circe
  val circeParser    = "io.circe"              %% "circe-parser"    % V.circe
  val logbackClassic = "ch.qos.logback"         % "logback-classic" % V.logback
  val pureconfig     = "com.github.pureconfig" %% "pureconfig"      % V.pureconfig
  val catsEffect     = "org.typelevel"         %% "cats-effect"     % V.cats

  val dependencies: Seq[ModuleID] = Seq(
    circeCore,
    circeGenerics,
    circeParser,
    pureconfig,
    catsEffect,
    logbackClassic % Runtime
  )

}

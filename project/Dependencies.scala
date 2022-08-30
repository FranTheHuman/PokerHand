import sbt._

object Dependencies {

  object V {
    val circe      = "0.14.2"
    val logback    = "1.2.11"
    val log4cats   = "2.4.0"
    val pureconfig = "0.17.1"
    val cats       = "3.3.14"
    val http4s     = "0.23.15"
    val doobie     = "1.0.0-RC1"
  }

  val circeCore         = "io.circe"              %% "circe-core"          % V.circe
  val circeGenerics     = "io.circe"              %% "circe-generic"       % V.circe
  val circeParser       = "io.circe"              %% "circe-parser"        % V.circe
  val logbackClassic    = "ch.qos.logback"         % "logback-classic"     % V.logback
  val log4catsCore      = "org.typelevel"         %% "log4cats-core"       % V.log4cats
  val log4catsSlf4j     = "org.typelevel"         %% "log4cats-slf4j"      % V.log4cats
  val pureconfig        = "com.github.pureconfig" %% "pureconfig"          % V.pureconfig
  val catsEffect        = "org.typelevel"         %% "cats-effect"         % V.cats
  val http4sDsl         = "org.http4s"            %% "http4s-dsl"          % V.http4s
  val http4sEmberServer = "org.http4s"            %% "http4s-ember-server" % V.http4s
  val http4sCirce       = "org.http4s"            %% "http4s-circe"        % V.http4s
  val circeGeneric      = "io.circe"              %% "circe-generic"       % V.circe
  val circeLiteral      = "io.circe"              %% "circe-literal"       % V.circe
  val doobieCore        = "org.tpolecat"          %% "doobie-core"         % V.doobie
  val doobieHikari      = "org.tpolecat"          %% "doobie-hikari"       % V.doobie
  val postgresDriver    = "org.tpolecat"          %% "doobie-postgres"     % V.doobie

  val dependencies: Seq[ModuleID] = Seq(
    circeCore,
    circeGenerics,
    circeParser,
    pureconfig,
    log4catsCore,
    log4catsSlf4j,
    catsEffect,
    http4sDsl,
    http4sEmberServer,
    http4sCirce,
    circeGeneric,
    circeLiteral,
    doobieCore,
    doobieHikari,
    postgresDriver,
    logbackClassic % Runtime
  )

}

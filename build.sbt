name := """playframework-2.4.x-scala"""

version := "1.0-SNAPSHOT"

//@see 3.3.Custom format @+2
//lazy val root = (project in file(".")).enablePlugins(PlayScala)
lazy val root = (project in file(".")).enablePlugins(PlayScala,SbtTwirl)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


// fork in run := true

// @see 3.3.Custom format @+5
import play.twirl.sbt.Import._

TwirlKeys.templateFormats += ("shi" -> "controllers.c3.A9.ShiFormat")

TwirlKeys.templateImports += "controllers.c3.A9._"


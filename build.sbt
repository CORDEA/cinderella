name := """cinderella"""
organization := "jp.cordea"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "com.github.nscala-time" %% "nscala-time" % "2.18.0"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "jp.cordea.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "jp.cordea.binders._"

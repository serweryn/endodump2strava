organization := "endodump2strava"
name := "importer"
version := "0.1-SNAPSHOT"

scalaVersion := "2.11.12"

lazy val root = (project in file("."))
  .aggregate(api)
  .settings(
    libraryDependencies ++= Seq(
      "com.beachape" %% "enumeratum" % "1.6.1",
      "com.typesafe.play" %% "play-json" % "2.7.4",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.scalatest" %% "scalatest" % "3.2.3" % Test
    )
  )

lazy val api = project in file("strava-api")

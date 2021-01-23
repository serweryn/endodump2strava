organization := "endodump2strava"
name := "importer"
version := "0.1-SNAPSHOT"

scalaVersion := "2.13.4"

lazy val root = (project in file("."))
  .aggregate(api)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json" % "2.9.2",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.scalatest" %% "scalatest" % "3.2.3" % Test
    )
  )

lazy val api = project in file("strava-api")

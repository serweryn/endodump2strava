organization := "endodump2strava"
name := "importer"
version := "0.1-SNAPSHOT"

scalaVersion := "2.11.12"

val jacksonVersion = "2.9.8"
val overriden = Seq(
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "joda-time" % "joda-time" % "2.10.1",
)

lazy val root = (project in file("."))
  .aggregate(api)
  .dependsOn(api)
  .settings(
    libraryDependencies ++= Seq(
      "com.beachape" %% "enumeratum" % "1.6.1",
      "com.typesafe.play" %% "play-json" % "2.7.4",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
//      "org.scalatest" %% "scalatest" % "3.2.3" % Test
    )
  )

lazy val api = (project in file("strava-api"))
  .settings(
    libraryDependencies ++= overriden
  )

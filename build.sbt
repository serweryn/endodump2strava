organization := "endodump2strava"
name := "importer"
version := "0.1-SNAPSHOT"

scalaVersion := "2.11.12"

val jacksonVersion = "2.9.8"
val overrides = Seq(
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "joda-time" % "joda-time" % "2.10.1",
  "com.typesafe" % "config" % "1.4.1",
)

lazy val importer = (project in file("."))
  .aggregate(`strava-api`)
  .dependsOn(`strava-api`)
  .settings(
    libraryDependencies ++= Seq(
      "com.beachape" %% "enumeratum" % "1.6.1",
      "com.typesafe.play" %% "play-json" % "2.7.4",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.h2database" % "h2" % "1.4.200",
      "io.getquill" %% "quill-jdbc" % "3.6.0",
//      "org.scalatest" %% "scalatest" % "3.2.3" % Test
    ),
    dependencyOverrides ++= overrides
  )

lazy val `strava-api` = (project in file("strava-api"))
  .settings(
    dependencyOverrides ++= overrides
  )

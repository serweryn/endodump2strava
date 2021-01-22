version := "3-SNAPSHOT"
name := "strava-api"
organization := "endodump2strava"
scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.2",
  "com.typesafe.akka" %% "akka-actor" % "2.6.11",
  "io.spray" % "spray-client" % "1.3.1",
  "joda-time" % "joda-time" % "2.9.9",
  "org.json4s" %% "json4s-jackson" % "3.6.10",
  // test dependencies
  "org.scalatest" %% "scalatest" % "3.2.2" % "test",
  "junit" % "junit" % "4.12" % "test"
)

resolvers ++= Seq(Resolver.mavenLocal)

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

publishArtifact in (Compile, packageDoc) := false
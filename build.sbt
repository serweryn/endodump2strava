organization := "endodump2strava"
name := "loader"
version := "0.1-SNAPSHOT"

scalaVersion := "2.13.4"

lazy val root = (project in file("."))
  .aggregate(api)
  .dependsOn(api)

lazy val api = (project in file("strava-api"))

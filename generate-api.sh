#!/usr/bin/env bash

API_ARTIFACT="strava-api"

curl -X POST \
  https://generator3.swagger.io/api/generate \
  -H 'content-type: application/json' \
  -o ${API_ARTIFACT}.zip \
  -d '{
  "specURL" : "https://developers.strava.com/swagger/swagger.json",
  "lang" : "akka-scala",
  "type" : "CLIENT",
  "codegenVersion" : "V2",
  "options" : {
    "apiPackage": "endodump2strava.strava.api",
    "modelPackage": "endodump2strava.strava.model",
    "groupId": "endodump2strava",
    "artifactId": "strava-api",
    "artifactVersion": "3-SNAPSHOT"
  }
}'

if [[ -d ${API_ARTIFACT} ]]; then
  rm -r ${API_ARTIFACT}
fi
unzip -o ${API_ARTIFACT}.zip -d ${API_ARTIFACT}

mv ${API_ARTIFACT}/build.sbt ${API_ARTIFACT}/build.sbt.orig
sed  -e \
  's/^scalaVersion.*/scalaVersion := "2.13.4"/' \
  's/"com.typesafe.akka" %% "akka-actor" % .*/"com.typesafe.akka" %% "akka-actor" % "2.6.11",/' \
  's/"org.json4s" %% "json4s-jackson" % .*/"org.json4s" %% "json4s-jackson" % "3.6.10",/' \
  's/"org.scalatest" %% "scalatest" % [^%]*/"org.scalatest" %% "scalatest" % "3.2.2" /' \
  <${API_ARTIFACT}/build.sbt.orig \
  >${API_ARTIFACT}/build.sbt

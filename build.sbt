// shadow sbt-scalajs' crossProject and CrossType from Scala.js 0.6.x
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

crossScalaVersions in ThisBuild := Seq("2.11.12", "2.12.8")
scalaVersion in ThisBuild := crossScalaVersions.value.head

lazy val `scala-unboxed-option` = crossProject(JSPlatform, JVMPlatform).
  crossType(CrossType.Pure).
  in(file(".")).
  settings(
    name := "scala-unboxed-option",
    version := "0.1-SNAPSHOT",
    organization := "be.doeraene",
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-Xfatal-warnings",
      "-encoding", "utf8"
    ),

    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.1" % "test",
    scalacOptions in Test -= "-Xfatal-warnings"
  )

lazy val `scala-unboxed-optionJVM` = `scala-unboxed-option`.jvm
lazy val `scala-unboxed-optionJS` = `scala-unboxed-option`.js

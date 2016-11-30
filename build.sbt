crossScalaVersions in ThisBuild := Seq("2.11.8", "2.12.0")
scalaVersion in ThisBuild := crossScalaVersions.value.head

lazy val `scala-unboxed-option` = crossProject.crossType(CrossType.Pure).
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

import com.typesafe.sbt.web.Import.WebKeys

scalaVersion in ThisBuild := "2.12.4"
scalafmtOnCompile in ThisBuild := true

lazy val root = (project in file("."))
  .settings(
    organization := "org.scala-lang",
    version := "0.0.0-SNAPSHOT",
    resolvers += Resolver.sonatypeRepo("snapshots"),
    libraryDependencies ++= Seq(
      "ba.sake" %% "hepek" % "0.2.1-SNAPSHOT"
    ),
    (hepek in Compile) := {
      WebKeys.assets.value // run 'assets' after compiling...
      (hepek in Compile).value
    },
    WebKeys.webModulesLib := "site/lib"
  )
  .enablePlugins(HepekPlugin, SbtWeb)

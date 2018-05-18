val Http4sVersion = "0.18.11"
val Specs2Version = "4.2.0"
val LogbackVersion = "1.2.3"
val gatlingVersion = "2.3.0"

lazy val root = (project in file("."))
  .settings(
    organization := "com.maximum.moe",
    name := "benchiez",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.6",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.specs2" %% "specs2-core" % Specs2Version % "test",
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "io.netty" % "netty-handler" % "4.0.56.Final",
      "io.netty" % "netty-codec-http" % "4.0.56.Final",
      "io.netty" % "netty-transport-native-epoll" % "4.0.56.Final" classifier "linux-x86_64",
      "com.github.zainab-ali" %% "fs2-reactive-streams" % "0.5.1",
      "com.typesafe.netty" % "netty-reactive-streams-http" % "1.0.8",
      "io.chrisdavenport" %% "ember-core" % "0.0.1"
    ),
    scalacOptions := Seq(
      "-unchecked",
      "-feature",
      "-deprecation",
      "-encoding",
      "utf8",
      "-Ywarn-adapted-args",
      "-Ywarn-inaccessible",
      "-Ywarn-unused:imports",
      "-Ywarn-nullary-override",
      "-Ypartial-unification",
      "-language:higherKinds",
      "-language:implicitConversions"
    ),
    test in assembly := {},
  )

lazy val gatling = (project in file("gatling"))
  .dependsOn(root)
  .settings(libraryDependencies ++= Seq(
    "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "test",
    "io.gatling" % "gatling-test-framework" % gatlingVersion % "test"))
  .enablePlugins(GatlingPlugin)

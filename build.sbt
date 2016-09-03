import sbt.Keys._

name := "json"

version in Global := "0.1.0-SNAPSHOT"

description := "JSON <-> Map[String, Any]"

scalaVersion in Global := "2.11.8"

scalacOptions in Global ++= Seq(
  "-encoding", "UTF-8",
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yrangepos",
  "-language:postfixOps")

lazy val test = Project(
  "test",
  file("."),
  settings = Defaults.coreDefaultSettings ++ Seq(
    publishArtifact := false,
    resolvers ++= Seq(
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases",
      "Artima Maven Repository" at "http://repo.artima.com/releases"
    ),
    libraryDependencies ++= Seq(
      "com.storm-enroute" %% "scalameter" % "0.8-SNAPSHOT" % "test",
      "org.scalactic" %% "scalactic" % "3.0.0",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test"
    ),
    testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework"),
    parallelExecution in Test := false,
    logBuffered := false
  )
)


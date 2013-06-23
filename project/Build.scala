import sbt._
import Keys._

object ScaloidBuild extends Build {
  import ScaloidSettings._
  import Dependencies._

  lazy val basicSettings = Seq(
    version               := "2.1-8",
    organization          := "org.scaloid",
    organizationHomepage  := Some(new URL("http://blog.scaloid.org")),
    description           := "Less Painful Android Development with Scala",
    startYear             := Some(2012),
    scalaVersion          := "2.10.2",
    resolvers             ++= Dependencies.resolutionRepos,
    publishMavenStyle := true,
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    pomIncludeRepository := { _ => false },
    pomExtra := (
      <url>http://jsuereth.com/scala-arm</url>
      <licenses>
        <license>
          <name>The Apache Software License, Version 2.0</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:pocorall/scaloid.git</url>
        <connection>scm:git:git@github.com:pocorall/scaloid.git</connection>
		<developerConnection>scm:git:git@github.com:pocorall/scaloid.git</developerConnection>
      </scm>
      <developers>
        <developer>
          <id>pocorall</id>
          <name>Sung-Ho Lee</name>
          <email>pocorall@gmail.com</email>
        </developer>
      </developers>),
    scalacOptions         := Seq(
      "-target:jvm-1.6", "-deprecation", "-feature"
    ),
    javacOptions          ++= Seq(
      "-source", "1.6",
      "-target", "1.6"))

  // configure prompt to show current project
  override lazy val settings = super.settings :+ {
    shellPrompt := { s => Project.extract(s).currentProject.id + "> " }
  }

  // root project
//  lazy val parent = Project("parent", file("."))
//    .settings(scaloidSettings: _*)
//    .settings(publish := {}, publishLocal := {})
//    .aggregate(common, support_v4)

  lazy val common = Project("scaloid", file("scaloid-common"))
    .settings(name := "scaloid", exportJars := true)
    .settings(basicSettings: _*)
    .settings(scaloidSettings: _*)

  lazy val support_v4 = Project("scaloid-support-v4", file("scaloid-support-v4"))
    .settings(name := "scaloid-support-v4", exportJars := true)
    .settings(basicSettings: _*)
    .settings(scaloidSettings: _*)
    .settings(libraryDependencies += android_support_v4)
    .dependsOn(common)
}


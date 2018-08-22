name := """infra-jpa"""

organization := "br.com.infra"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice
libraryDependencies += javaJpa
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.23.1"
libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final"
libraryDependencies += "com.zsoltfabok" % "sqlite-dialect" % "1.0"
libraryDependencies += "br.com.infra" % "infra-core_2.12" % "1.0-SNAPSHOT"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

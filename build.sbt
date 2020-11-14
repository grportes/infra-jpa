name := """infra-jpa"""

organization := "br.com.infra"

version := "1.2"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .disablePlugins(PlayLogback)

scalaVersion := "2.13.1"

crossScalaVersions := Seq("2.11.12", "2.12.4", "2.12.8")

libraryDependencies += guice
libraryDependencies += javaJpa

// Hibernate:
libraryDependencies += "org.hibernate" % "hibernate-core" % "5.4.23.Final"

// Sqlite:
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.32.3.2"
libraryDependencies += "com.zsoltfabok" % "sqlite-dialect" % "1.0"

// Infra:
libraryDependencies += "br.com.infra" % "infra-core_2.13" % "1.2"

// Apache Commons:
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.11"
libraryDependencies += "org.apache.commons" % "commons-collections4" % "4.4"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

// Resolve only newly added dependencies
updateOptions := updateOptions.value.withCachedResolution(true)
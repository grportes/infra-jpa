##### == build.sbt ==

```
name := """infra-jpa"""
organization := "br.com.infra"
version := "1.0-SNAPSHOT"
scalaVersion := "2.12.6"
```

##### == Publicar ==

```
[infra-jpa] $ publishLocal
...

[info]  published infra-jpa_2.12 to /home/grportes/.ivy2/local/br.com.infra/infra-jpa_2.12/1.0-SNAPSHOT/poms/infra-jpa_2.12.pom
[info]  published infra-jpa_2.12 to /home/grportes/.ivy2/local/br.com.infra/infra-jpa_2.12/1.0-SNAPSHOT/jars/infra-jpa_2.12.jar
[info]  published infra-jpa_2.12 to /home/grportes/.ivy2/local/br.com.infra/infra-jpa_2.12/1.0-SNAPSHOT/srcs/infra-jpa_2.12-sources.jar
[info]  published infra-jpa_2.12 to /home/grportes/.ivy2/local/br.com.infra/infra-jpa_2.12/1.0-SNAPSHOT/docs/infra-jpa_2.12-javadoc.jar
[info]  published ivy            to /home/grportes/.ivy2/local/br.com.infra/infra-jpa_2.12/1.0-SNAPSHOT/ivys/ivy.xml
```

##### == Uso em outros projetos ==

```
libraryDependencies += "br.com.infra" % "infra-jpa_2.12" % "1.0-SNAPSHOT"
```
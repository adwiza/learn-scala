version := "0.1"

scalaVersion := "2.13.10"

lazy val sparkVersion = "3.3.1"

resolvers ++= Seq(
  "MavenRepository" at "https://mvnrepository.com"
)

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.2",
  "org.apache.spark" %% "spark-sql" % sparkVersion % Provided,
  "org.apache.spark" %% "spark-mllib" % sparkVersion % Provided,
  "org.apache.kafka" % "kafka-clients" % sparkVersion,
  "org.scalatestplus" %% "scalacheck-1-14" % "3.2.2.0",
  "org.scalatest" %% "scalatest" % "3.2.14",
  "org.scalacheck" %% "scalacheck" % "1.17.0",
  "org.typelevel" %% "cats-effect" % "3.4.4" withSources() withJavadoc(),
  "dev.zio" %% "zio" % "1.0.17",
  "dev.zio" %% "zio-streams" % "1.0.17",
  "dev.zio" %% "zio-test"    % "1.0.17"

)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps"
)

lazy val root = (project in file("."))
  .settings(
    name := "learn-scala"
  )

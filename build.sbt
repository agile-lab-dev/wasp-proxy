name := "wasp-proxy"

version := "1.0"

scalaVersion := "2.10.6"

val Akka = "2.3.14"
val Slf4j = "1.7.12"

enablePlugins(JavaAppPackaging)


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % Akka,
  "com.typesafe.akka" %% "akka-cluster" % Akka,
  "com.typesafe.akka" %% "akka-contrib" % Akka,
  "com.typesafe.akka" %% "akka-remote" % Akka,
  "com.typesafe.akka" %% "akka-slf4j" % Akka,
  "org.apache.kafka" %% "kafka" % "0.10.2.1",
  "io.confluent" % "kafka-rest" % "3.2.2" excludeAll(
    ExclusionRule(organization = "org.apache.kafka"),
    ExclusionRule(organization = "org.slf4j")
  ),
  "org.reactivemongo" %% "reactivemongo" % "0.11.14"
)

mainClass in Compile := Some("it.agilelab.bigdata.wasp.producers.App")


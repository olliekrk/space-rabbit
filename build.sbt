name := "space-rabbit"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  // https://mvnrepository.com/artifact/com.rabbitmq/amqp-client
  "com.rabbitmq" % "amqp-client" % "5.8.0",

  // https://mvnrepository.com/artifact/com.github.javafaker/javafaker
  "com.github.javafaker" % "javafaker" % "1.0.2",

  // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
  "org.slf4j" % "slf4j-api" % "1.7.30",

  // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
  "org.slf4j" % "slf4j-simple" % "1.7.30",

//  // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
//  "org.slf4j" % "slf4j-api" % "1.6.2",
//
//  // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
//  "org.slf4j" % "slf4j-simple" % "1.6.2",
//
  // https://mvnrepository.com/artifact/com.typesafe.scala-logging/scala-logging
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",


)

name := "TrainTicket"
 
version := "1.0" 
      
lazy val `trainticket` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.4"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice,
  ("com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % "2.8.10"),
  ("mysql" % "mysql-connector-java" % "6.0.6"),
//  ("mysql"%"mysql-connector-java"%"5.1.44"),
//  ("net.debasishg" %% "redisclient" % "3.4"),<dependency>
  ("org.redisson" % "redisson" % "3.5.5"),
//  ("org.neo4j" % "neo4j" % "3.3.1"),
//  ("org.neo4j" % "neo4j-kernel" % "3.3.1"),
  ("com.github.aselab" %% "scala-activerecord" % "0.4.0"),
  ("com.github.aselab" %% "scala-activerecord-play2" % "0.4.0"),
  ("com.github.aselab" %% "scala-activerecord-play2-specs" % "0.4.0")
//  ("com.typesafe.slick" %% "slick" % "3.2.1"),
//  ("org.slf4j" % "slf4j-nop" % "1.6.4"),
//  ("com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"),
//  ("com.typesafe.play" %% "play-slick" % "3.0.1"),
//  ("com.typesafe.play" %% "play-slick-evolutions" % "3.0.1")
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
val _name = "gateway-automation"

val _organization = "arch"

//this is the api version and should only be integers, only increment this for breaking changes to your service
val _version = "1"

name := _name

organization := _organization

version := _version

startYear := Some(2015)

/* scala versions and options */
scalaVersion := "2.11.6"

scalacOptions ++= Seq(
  "-Yclosure-elim",
  "-Yinline"
)

// These language flags will be used only for 2.10.x.
scalacOptions <++= scalaVersion map { sv =>
  if (sv startsWith "2.11") List(
    "-Xverify"
    ,"-feature"
    ,"-language:postfixOps"
  )
  else Nil
}

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

val akka = "2.3.11"
val spray = "1.3.3"

/* dependencies */
libraryDependencies ++= Seq (
  // -- Logging --
  "ch.qos.logback"              % "logback-classic"     % "1.1.2"
  ,"com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
  // -- Akka --
  ,"com.typesafe.akka"          %% "akka-testkit"        % akka     % "test"
  ,"com.typesafe.akka"          %% "akka-actor"          % akka
  ,"com.typesafe.akka"          %% "akka-slf4j"          % akka
  // -- Spray --
  ,"io.spray"                   %% "spray-routing"       % spray
  ,"io.spray"                   %% "spray-client"        % spray
  ,"io.spray"                   %% "spray-testkit"       % spray    % "test"
  // -- json --
  ,"io.spray"                   %%  "spray-json"         % "1.3.2"
  // -- config --
  ,"com.typesafe"                % "config"              % "1.2.1"
  // -- database --
  ,"com.typesafe.slick"         %% "slick"               % "2.1.0"
  ,"org.postgresql"              % "postgresql"          % "9.3-1100-jdbc41"
  // -- time --
  ,"joda-time"                   % "joda-time"           % "2.7"
  ,"org.joda"                    % "joda-convert"        % "1.7"
  ,"com.github.tototoshi"       %% "slick-joda-mapper"   % "1.2.0"
)

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io",
  "splunk repo" at "http://splunk.artifactoryonline.com/splunk/ext-releases-local",
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.url("bintray-tecsisa-repo", url("http://dl.bintray.com/tecsisa/maven-bintray-repo"))(Resolver.ivyStylePatterns)
)
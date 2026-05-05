val scala3Version = "3.3.3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "re-archive",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.3.0" % Test,
      "org.apache.commons" % "commons-compress" % "1.26.1",
      "com.github.junrar"  % "junrar"           % "7.5.5"
    ),

    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", "versions", xs @ _*) => MergeStrategy.first
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.defaultMergeStrategy(x)
    }

  )

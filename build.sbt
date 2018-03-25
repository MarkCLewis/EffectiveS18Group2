lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "edu.trinity",
			scalaVersion := "2.12.4",
      		version      := "0.1.0-SNAPSHOT"
    )),
    name := "EffectiveS18Group2",
	crossPaths := false,
	javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= Seq(
			"org.apache.commons" % "commons-math3" % "3.1.1",
			"com.novocode" % "junit-interface" % "0.11" % Test,
			"org.joml" % "joml" % "1.9.8",
			"org.joml" % "joml-2d" % "1.6.0"
		)
  )

fork := true


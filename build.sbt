

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "edu.trinity",
			scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "EffectiveS18Group1",
		crossPaths := false,
    libraryDependencies ++= Seq(
			"org.apache.commons" % "commons-math3" % "3.1.1",
			"com.novocode" % "junit-interface" % "0.11" % Test
		)
  )

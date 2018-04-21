lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "edu.trinity",
			scalaVersion := "2.12.4",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "EffectiveS18Group2",
  	crossPaths := false,
    resolvers += "jMonkeyEngine" at "https://dl.bintray.com/jmonkeyengine/org.jmonkeyengine/",
  	javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= Seq(
  		"org.apache.commons" % "commons-math3" % "3.1.1",
  		"com.novocode" % "junit-interface" % "0.11" % Test,
      //"junit" % "junit" % "3.8.1",
  		"org.joml" % "joml" % "1.9.8",
  		"org.joml" % "joml-2d" % "1.6.0",
      "org.jmonkeyengine" % "jme3-blender" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-bullet-native" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-bullet" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-core" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-desktop" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-effects" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-jogg" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-jogl" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-lwjgl" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-lwjgl3" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-networking" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-niftygui" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-plugins" % "3.2.1-stable",
      "org.jmonkeyengine" % "jme3-terrain" % "3.2.1-stable"
  	)
  )

fork := true

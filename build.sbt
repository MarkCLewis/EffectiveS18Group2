lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "edu.trinity",
			scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "EffectiveS18Group2",
		crossPaths := false,
    libraryDependencies ++= Seq(
			"org.apache.commons" % "commons-math3" % "3.1.1",
			"com.novocode" % "junit-interface" % "0.11" % Test,
            		"org.lwjgl" % "lwjgl" % "3.1.6",
			"org.lwjgl" % "lwjgl-glfw" % "3.1.6",
			"org.lwjgl" % "lwjgl-opengl" % "3.1.6",
			"org.lwjgl" % "lwjgl-openal" % "3.1.6",
			"org.lwjgl" % "lwjgl-stb" % "3.1.6",
			"org.lwjgl" % "lwjgl-vulkan" % "3.1.6",
			"org.lwjgl" % "lwjgl-xxhash" % "3.1.6",
			"org.lwjgl" % "lwjgl-bgfx" % "3.1.6",
			"org.lwjgl" % "lwjgl-sse" % "3.1.6",
			"org.lwjgl" % "lwjgl-par" % "3.1.6",
			"org.joml" % "joml" % "1.9.8",
			"org.joml" % "joml-2d" % "1.6.0"
		)
  )

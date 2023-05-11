lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    organization := "com.example",
    name := "ctd-omega-services-with-supervisor",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
        // "core" module - IO, IOApp, schedulers
        // This pulls in the kernel and std modules automatically.
        "org.typelevel" %% "cats-effect" % "3.4.8",
        // concurrency abstractions and primitives (Concurrent, Sync, Async etc.)
        "org.typelevel" %% "cats-effect-kernel" % "3.4.7",
        // standard "effect" library (Queues, Console, Random etc.)
        "org.typelevel" %% "cats-effect-std" % "3.4.7",

        // better monadic for compiler plugin as suggested by documentation
        compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),

        "com.disneystreaming" %% "weaver-cats" % "0.8.3" % Test
  ),
  testFrameworks += new TestFramework("weaver.framework.CatsEffect")
)

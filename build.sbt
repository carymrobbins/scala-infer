name := "infer"

scalaVersion := "2.12.1"

scalacOptions ++= Seq(
  "-language:implicitConversions",
  "-language:reflectiveCalls"
)

libraryDependencies ++= Seq(
) ++ testDependencies

lazy val testDependencies = Seq(
  "org.scalacheck" %% "scalacheck" % "1.13.4",
  "org.scalatest" %% "scalatest" % "3.0.0"
).map(_ % "test")

lazy val foo = project in file(".") dependsOn (a, ProjectRef(file("bar"), "bar"), RootProject(file("baz")))

val a = project

TaskKey[Unit]("check") := check(target.value / "projects-graph.dot", baseDirectory.value / "projects-graph.dot")

def check(inc0: File, exp0: File) = {
  val inc = IO readLines inc0
  val exp = IO readLines exp0
  assert(inc == exp, s"Graph mismatch:\n${unifiedDiff(exp, inc) mkString "\n"}")
}

def unifiedDiff(expected: Seq[String], obtained: Seq[String], contextSize: Int = 3): Vector[String] = {
  import scala.collection.JavaConverters._
  val patch = difflib.DiffUtils.diff(expected.asJava, obtained.asJava)
  difflib.DiffUtils.generateUnifiedDiff("expected", "obtained", expected.asJava, patch, contextSize).asScala.toVector
}

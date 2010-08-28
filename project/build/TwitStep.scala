import sbt._

class TwitStep(info: ProjectInfo) extends DefaultProject(info) /* with AkkaProject */ {
  
  val twitter = "net.unto.twitter" % 
                "java-twitter" % 
                "0.9-SNAPSHOT"
  
  val twitterRep = "Java Twitter Rep" at 
                "http://java-twitter.googlecode.com/svn/repository/"
  
  val mavenLocal = "Local Maven Repository" at 
                "file://"+Path.userHome+"/.m2/repository"
}
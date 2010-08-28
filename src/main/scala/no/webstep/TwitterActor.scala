package no.webstep

import se.scalablesolutions.akka.actor.Actor
import se.scalablesolutions.akka.actor.ActorRegistry.actorFor
import se.scalablesolutions.akka.actor.Actor._
import java.util.Properties
import scala.collection.JavaConversions._

class TwitterActor extends Actor {
  
  def subscriptionActor = actorFor[SubscriptionActor].head
  
  /*
  val props = new Properties
  props.load(getClass.getResourceAsStream("twitter.properties"))
  */
  
  
  def receive = {
    case Last => 
      log.info("Last")
      self.reply(
        subscriptionActor !! SubscriberList match {
          case Some(l: Set[String]) => 
            l.map { name =>
              (name, actorOf[TwitterComm].start !!! LastMessage(name))
            } map { 
              _ match {
                case (name, future) =>
                  future.await
                  future.result.asInstanceOf[Option[String]] map {
                    msg => (name, msg)
                  } 
              }
            } flatten
          case None => Set()
        }
      )
  }
}


class TwitterComm extends Actor {

  import net.unto.twitter.Api
  import net.unto.twitter.TwitterProtos.Status
  
  val api = Api.DEFAULT_API

  def receive = {
    case LastMessage(name) =>
      self.reply(
        api.search("from:" + name).sinceId(0).
          build.get.getResultsList.toList match {
            case res :: _ => res.getText
            case Nil => ""
          }
      )
  }
}

case object Last
case class LastMessage(name: String)

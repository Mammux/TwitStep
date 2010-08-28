package no.webstep

import javax.ws.rs._
import se.scalablesolutions.akka.util.Logging
import se.scalablesolutions.akka.actor.ActorRegistry.actorFor
import se.scalablesolutions.akka.actor.Actor._

@Path("/twitstep")
class Service extends Logging with PrettyXml {
  
  def subscriptionActor = actorFor[SubscriptionActor].head
  def twitterActor = actorFor[TwitterActor].head
  
  @GET
  @Path("/test")
  @Produces(Array("application/xml"))
  def test = <greeting>Time: {new java.util.Date().toString}</greeting>
  
  @GET
  @Path("/subscribe/{id}")
  @Produces(Array("application/xml"))
  def subscribe(@PathParam("id") id: String) = {
    log.info("subscribe")
    actorFor[SubscriptionActor].foreach ( _ ! Subscribe(id))
    <ok/>
  }

  @GET
  @Path("/unsubscribe/{id}")
  @Produces(Array("application/xml"))
  def unsubscribe(@PathParam("id") id: String) = {
    log.info("unsubscribe: " + id)
    actorFor[SubscriptionActor].foreach ( _ ! Unsubscribe(id))
    <ok/>
  }

  @GET
  @Path("/list")
  @Produces(Array("application/xml"))
  def list(@PathParam("id") id: String) = {    
    <list>
    { subscriptionActor !! SubscriberList match {
      case Some(s: Set[String]) => s.map { id => <id>{id}</id>}
      case None => <error>No connection</error>
    } }
    </list> pretty
  }
    
  @GET
  @Path("/last")
  @Produces(Array("application/xml"))
  def last = 
    <last>
    { 
      twitterActor !! (Last, 15000) match {
        case Some(l: Set[(String,String)]) => l.map {
          _ match {
            case Pair(user, msg) => <msg><id>{user}</id><text>{msg}</text></msg>            
          }
        }
        case None => Nil
      }  
    }
    </last> pretty

}
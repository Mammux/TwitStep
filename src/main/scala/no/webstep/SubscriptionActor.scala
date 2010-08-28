package no.webstep

import se.scalablesolutions.akka.actor.Actor

class SubscriptionActor extends Actor {
  var subscriptions: Set[Subscription] = 
    Set("Mammux","wikileaks","mbknor").map(Subscription(_))
  def receive = {
    case Subscribe(id) => 
      log.info("Subscribe(" + id + ")")
      subscriptions += Subscription(id)
    case Unsubscribe(id) => 
      log.info("Unsubscrive(" + id + ")")
      subscriptions -= Subscription(id)
    case SubscriberList => 
      log.info("SubscriberList")
      self.reply(subscriptions.map(_.id))
  }
  
}

case class Subscribe(id: String)
case class Unsubscribe(id: String)
case object SubscriberList
case object Subscribers

case class Subscription(id: String)

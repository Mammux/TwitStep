package no.webstep

import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.actor.Actor._
import se.scalablesolutions.akka.config.ScalaConfig._

class Boot {

  val factory = SupervisorFactory(
    SupervisorConfig(
      RestartStrategy(OneForOne, 3, 100, List(classOf[Exception])),
      List(actorOf[SubscriptionActor], actorOf[TwitterActor]).
        map(Supervise( _, LifeCycle(Permanent)))))
 
  factory.newInstance.start

}
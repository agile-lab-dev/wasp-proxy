package it.agilelab.bigdata.wasp.core

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import it.agilelab.bigdata.wasp.core.utils._

import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration


object WaspSystem {

  var alreadyInit = false

  def systemInitialization(actorSystem: ActorSystem, force: Boolean = false) = {
    if (!alreadyInit || force) {
      alreadyInit = true
    }
  }

  /**
   * Timeout value for actor's syncronous call (ex. 'actor ? msg') 
   */
  val conf = ConfigFactory.load
  implicit val timeout = Timeout(60, TimeUnit.SECONDS)

  /**
   * WASP actor system.
   * Initialized by trait ActorSystemInjector through initializeActorSystem.
   */
  implicit var actorSystem: ActorSystem = _

  /**
    * Initializes the actor system if needed.
    *
    * @note Only the first call will initialize the actor system; following attempts at initialization
    *       even if with different settings will not have any effect and will silently be ignored.
    */
  def initializeActorSystem(actorSystemName: String): Unit = {
    /*
    We check for a null (not initialized) actor system two times:
    - one outside the synchronized block, so this method is cheap to call as it will be invoked
      when instantiating anything mixing in the ActorSystemInjector trait
    - one inside the synchronized block, as the outside one does not guarantee that it has not been
      initialized by someone else while we were blocked on the synchronized
     */
    if (actorSystem == null) WaspSystem.synchronized {
      if (actorSystem == null) {
        actorSystem = ActorSystem(actorSystemName)
      }
    }
  }

  /**
   * WASP logger actor.
   * Initialized by trait LoggerInjector through initializeLoggerActor.
   */
  var loggerActor: Option[ActorRef] = _

  /**
    * Initializes the logger actor if needed; safe to call multiple times.
    *
    * @note Only the first call will initialize the logger actor; following attempts at initialization
    *       even if with different settings will not have any effect and will silently be ignored.
    */
  def initializeLoggerActor(loggerActorProps: Props, loggerActorName: String): Unit = {
    /*
    We check for a null (not initialized) logger actor two times:
    - one outside the synchronized block, so this method is cheap to call as it will be invoked
      when instantiating anything mixing in the LoggerInjector trait
    - one inside the synchronized block, as the outside one does not guarantee that it has not been
      initialized by someone else while we were blocked on the synchronized
     */
    if (loggerActor == null) WaspSystem.synchronized {
      if (loggerActor == null) {
        if (actorSystem == null) {
          loggerActor = None
        } else {
          /*val actorPath = actorSystem / "InternalLogProducerGuardian"
					val future = actorSystem.actorSelection(actorPath).resolveOne()
					Some(Await.result(future, timeout.duration))*/
          loggerActor = Some(actorSystem.actorOf(loggerActorProps, loggerActorName))
        }
      }
    }
  }

  //def loggerActorProps = Props[InternalLogProducerGuardian]
  var masterActor: ActorRef = _


  def now = System.currentTimeMillis

  /**
   * Unique global shutdown point.
   */
  def shutdown() = {

    // close actor system
    if (actorSystem != null)
      actorSystem.shutdown()

  }

  def ??[T](actorReference: ActorRef, message: WaspMessage, duration: Option[FiniteDuration] = None) =
    Await.result(actorReference ? message, duration.getOrElse(timeout.duration)).asInstanceOf[T]

}

trait WaspSystem extends ActorSystemInjector {}

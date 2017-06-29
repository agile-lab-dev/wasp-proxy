package it.agilelab.bigdata.wasp.producers


import akka.actor.Props
import akka.cluster.{Cluster, MemberStatus}
import akka.contrib.pattern.ClusterClient.Publish
import akka.contrib.pattern.DistributedPubSubExtension
import it.agilelab.bigdata.wasp.core.WaspSystem
import it.agilelab.bigdata.wasp.core.messages.{AddRemoteProducer, StartProducer}
import it.agilelab.bigdata.wasp.launcher.WaspLauncher

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._



/**
  * Created by Agile Lab s.r.l. on 29/06/2017.
  */
object App extends WaspLauncher {

  /**
    * Launchers must override this with deployment-specific pipegraph initialization logic;
    * this usually simply means loading the custom pipegraphs into the database.
    */
  override def initializeCustomWorkloads(args: Array[String]): Unit = {

    // get mediator

    val cluster = Cluster(WaspSystem.actorSystem)
    val producerClass = classOf[KafkaProxyProducer]
    println(s"Starting producer $producerClass")
    val producerActor = WaspSystem.actorSystem.actorOf(Props(producerClass), "KafkaProxyProducer")

    producerActor ! StartProducer

    lazy val startProducersCallback: () => Unit = () => {
      val members = cluster.state.members.filter(_.status == MemberStatus.Up)

      if (members.exists(m => m.roles.contains("master"))) { // WASP "head" is in the cluster!
        println("Master found, starting actors")
        // start all producers
        val producerIds = args.toList
        producerIds foreach {
          producerId => {
            println(s"Retrieving producer $producerId")
            println(s"Started producer $producerClass; actor reference: $producerActor")
            val mediator = DistributedPubSubExtension.get(WaspSystem.actorSystem).mediator

            mediator ! Publish(producersAkkaTopic, AddRemoteProducer(producerId, producerActor))

            println(s"Published add remote producer request.")
          }
        }
      } else { // no WASP "head" in cluster
        println("No master yet")
        // reschedule
        WaspSystem.actorSystem.scheduler.scheduleOnce(5.seconds)(startProducersCallback())
      }

    }

    //cluster.registerOnMemberUp[Unit](startProducersCallback())
    // schedule
    WaspSystem.actorSystem.scheduler.scheduleOnce(5.seconds)(startProducersCallback())
  }

}



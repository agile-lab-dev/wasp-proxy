package it.agilelab.bigdata.wasp.producers

import akka.actor.Actor
import akka.event.slf4j.Logger
import com.typesafe.config.ConfigFactory
import io.confluent.kafkarest.{KafkaRestApplication, KafkaRestConfig}
import io.confluent.rest.RestConfigException
import it.agilelab.bigdata.wasp.core.messages._
import it.agilelab.bigdata.wasp.core.utils.ConfigUtils
import it.agilelab.bigdata.wasp.producers.cluster.ClusterAwareNodeGuardian

/**
  * Created by Agile Lab s.r.l. on 27/06/2017.
  */

class KafkaProxyProducer extends ClusterAwareNodeGuardian {
  val logger = Logger(getClass.toString)
  val name: String = "KafkaProxyProducer"


  var app: KafkaRestApplication = _

  override def postStop(): Unit = {
    super.postStop()
  }

  override def preStart(): Unit = {
    //mediator ! Subscribe(producers, self)
    super.preStart()
  }

  override def initialized: Receive = {
    case StartProducer =>
      logger.info("already startProducer")
      sender() ! true

    case StopProducer =>
      logger.info("StopProducer")
      app.stop()
      stopChildActors()
      sender() ! true
  }

  override def uninitialized: Actor.Receive = super.uninitialized orElse guardianUnitialized

  def guardianUnitialized: Actor.Receive = {
    case StartProducer =>
      logger.info("startProducer")
      initialize()
      app.start()
      sender() ! true

    case StopProducer =>
      logger.info("already stopProducer")
      sender() ! true
  }

  def stopChildActors() = {
    context become guardianUnitialized
  }

  override def initialize(): Unit = {

    if (app == null) {
      try {
        val conf = ConfigFactory.load()
        val configRestKafka = ConfigUtils.propsFromConfig(conf.getConfig("kafka.rest"))

        val kafkaConfig = new KafkaRestConfig(configRestKafka)
        app = new KafkaRestApplication(kafkaConfig)

        log.info("Server started, listening for requests...")
        context become initialized
      } catch {
        case e: RestConfigException =>
          log.error("Server configuration failed: ", e)
          throw e
        case e: Exception =>
          log.error("Server died unexpectedly: ", e)
          throw e
      }
    }

    super.initialize()
  }
}


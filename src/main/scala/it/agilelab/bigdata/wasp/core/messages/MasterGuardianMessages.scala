package it.agilelab.bigdata.wasp.core.messages

import akka.actor.ActorRef
import it.agilelab.bigdata.wasp.core.WaspMessage

trait MasterGuardianMessage extends WaspMessage { val id: String }

trait PipegraphMessage extends MasterGuardianMessage
trait ProducerMessage extends MasterGuardianMessage
trait ETLMessage extends MasterGuardianMessage { val etlName: String }
trait BatchJobMessage extends MasterGuardianMessage

case class RemovePipegraph(override val id: String) extends PipegraphMessage
case class StartPipegraph(override val id: String) extends PipegraphMessage
case class StopPipegraph(override val id: String) extends PipegraphMessage
case object RestartPipegraphs extends MasterGuardianMessage { val id = null }
@SerialVersionUID(1000L)
case class AddRemoteProducer(override val id: String, remoteProducer: ActorRef) extends ProducerMessage
@SerialVersionUID(1001L)
case class RemoveRemoteProducer(override val id: String, remoteProducer: ActorRef) extends ProducerMessage
@SerialVersionUID(1002L)
case class StartProducer(override val id: String) extends ProducerMessage
@SerialVersionUID(1003L)
case class StopProducer(override val id: String) extends ProducerMessage
case class StartETL(override val id: String, override val etlName: String) extends ETLMessage
case class StopETL(override val id: String, override val etlName: String) extends ETLMessage

case class StartBatchJob(override val id: String) extends BatchJobMessage
case class StartPendingBatchJobs(override val id: String) extends MasterGuardianMessage
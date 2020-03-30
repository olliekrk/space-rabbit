package space.actors

import com.rabbitmq.client.Channel
import rabbitmq.RabbitConnectivity._
import space._
import space.messaging.{SpaceTask, SpaceTaskType}
import util.FakeUtils._
import util.SerializationUtils._
import util.TryUtils._

import scala.io.StdIn

case class SpaceAgency(override val name: String)(implicit override val channel: Channel) extends SpaceInfoConsumer {

  override val infoQueueName = s"spaceAgency_$name"

  override val infoQueueKeys = Seq(
    s"$SPACE_AGENCY_ROUTING_KEY.$name",
    SPACE_AGENCY_ROUTING_KEY,
    SPACE_ROUTING_KEY
  )

  def produceTask(taskType: SpaceTaskType): Unit = {
    val task = SpaceTask(taskType, name, fakeDescription)
    val serializedTask = task.toJSON.getBytes(SPACE_CHARSET)
    channel.basicPublish(SPACE_EXCHANGE_NAME, taskType.routingKey, null, serializedTask)
    logger.info(s"Successfully scheduled new task: $task to: ${taskType.routingKey}")
  }

}

object SpaceAgency extends App {

  tryWith(createConnection) { connection =>
    tryWith(connection.createChannel()) { implicit channel =>
      channel
        .declareTopicExchange(SPACE_EXCHANGE_NAME)
        .basicQos(1)

      val agency = SpaceAgency(sanitizeName(fakeCompany))
      agency.consumeInfo()

      while (true) {
        println(s"Provide next task type: (${SpaceTaskType.hint})")
        SpaceTaskType.byName.get(StdIn.readLine()) match {
          case Some(taskType) => agency.produceTask(taskType)
          case None => println("Invalid task type.")
        }
      }

    }
  }

}

package space.actors

import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import rabbitmq.RabbitConnectivity._
import space._
import space.messaging.SpaceTaskType
import util.FakeUtils._
import util.TryUtils._

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

case class SpaceCarrier(override val name: String)(implicit override val channel: Channel)
  extends SpaceInfoConsumer with SpaceInfoProducer {

  override val infoQueueName = s"spaceCarrier_$name"

  override val infoQueueKeys = Seq(
    s"$SPACE_CARRIER_ROUTING_KEY.$name",
    SPACE_CARRIER_ROUTING_KEY,
    SPACE_ROUTING_KEY
  )

  def tasksConsumer(): DefaultConsumer = new DefaultConsumer(channel) {
    override def handleDelivery(consumerTag: String,
                                envelope: Envelope,
                                properties: AMQP.BasicProperties,
                                body: Array[Byte]): Unit = Try {
      // todo: deserialize SpaceTask from body & log
      new String(body, SPACE_CHARSET)
      // todo: extract agency name from message
    } match {
      case Success(message) =>
        channel.basicAck(envelope.getDeliveryTag, false)
        produceInfo(s"Carrier: $name completed: $message", s"$SPACE_AGENCY_ROUTING_KEY.TODO")
      case Failure(exception) =>
        logger.warn(s"Failed to complete task: ${exception.getMessage}")
    }
  }

  def consumeTasks(taskTypes: Iterable[SpaceTaskType]): Unit =
    taskTypes.foreach { taskType =>
      declareAndBindQueue(taskType.queueName, SPACE_EXCHANGE_NAME, Option(taskType.routingKey))
      consumeFromQueue(taskType.queueName, tasksConsumer())
    }

}

object SpaceCarrier extends App {

  println(s"Provide 2 task types for this carrier: (${SpaceTaskType.hint})")
  val taskTypes = Seq(StdIn.readLine(), StdIn.readLine()).flatMap(SpaceTaskType.byName.get).distinct

  if (taskTypes.length != 2) {
    println("Invalid task types were provided")
    System.exit(1)
  }

  tryWith(createConnection) { connection =>
    tryWith(connection.createChannel()) { implicit channel =>
      channel
        .declareTopicExchange(SPACE_EXCHANGE_NAME)
        .basicQos(1)

      val carrier = SpaceCarrier(sanitizeName(fakeCompany))
      carrier.consumeInfo()
      carrier.consumeTasks(taskTypes)

      while (true) {
        Thread.sleep(1_000)
      }

    }
  }

}

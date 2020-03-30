package space.actors

import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import rabbitmq.RabbitConnectivity.{createConnection, sanitizeName, _}
import space.messaging.{SpaceInfo, SpaceInfoType}
import space.{SPACE_CHARSET, SPACE_EXCHANGE_NAME, SPACE_ROUTING_KEY}
import util.FakeUtils.fakeCompany
import util.TryUtils.tryWith

import scala.io.StdIn

case class SpaceAdmin(override val name: String)(implicit override val channel: Channel)
  extends SpaceInfoConsumer with SpaceInfoProducer {

  override val infoQueueName = s"spaceAdmin_$name"

  override val infoQueueKeys = Seq(
    s"$SPACE_ROUTING_KEY.#"
  )

  // override to handle raw JSON strings instead of deserialization
  override def infoConsumer(): DefaultConsumer = new DefaultConsumer(channel) {
    override def handleDelivery(consumerTag: String,
                                envelope: Envelope,
                                properties: AMQP.BasicProperties,
                                body: Array[Byte]): Unit =
      try {
        val rawMessage = new String(body, SPACE_CHARSET)
        channel.basicAck(envelope.getDeliveryTag, false)
        logger.info(s"Received raw info: $rawMessage")
      } catch {
        case ex: Exception => logger.warn(s"Failed to receive info: ${ex.getMessage}")
      }
  }
}

object SpaceAdmin extends App {

  tryWith(createConnection) { connection =>
    tryWith(connection.createChannel()) { implicit channel =>
      channel
        .declareTopicExchange(SPACE_EXCHANGE_NAME)
        .basicQos(1)

      val admin = SpaceAdmin(sanitizeName(fakeCompany))
      admin.consumeInfo()

      while (true) {
        println(s"Provide next admin broadcast type: (${SpaceInfoType.hint})")
        SpaceInfoType.byName.get(StdIn.readLine()) match {
          case Some(infoType) =>
            println("Provide message:")
            val infoMessage = s"[ADMIN: ${admin.name}][SCOPE: ${infoType.routingKey}]: ${StdIn.readLine()}"
            admin.produceInfo(SpaceInfo(infoType, infoMessage))
          case None =>
            println("Invalid admin broadcast type.")
        }
      }

    }
  }

}

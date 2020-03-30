package space.actors

import com.rabbitmq.client.Channel
import rabbitmq.RabbitConnectivity.{createConnection, sanitizeName, _}
import space.messaging.AdminBroadcastType
import space.{SPACE_CHARSET, SPACE_EXCHANGE_NAME, SPACE_ROUTING_KEY}
import util.FakeUtils.fakeCompany
import util.TryUtils.tryWith

import scala.io.StdIn

case class SpaceAdmin(override val name: String)(implicit override val channel: Channel) extends SpaceInfoConsumer {

  override val infoQueueName = s"spaceAdmin_$name"

  override val infoQueueKeys = Seq(
    s"$SPACE_ROUTING_KEY.#"
  )

  def broadcastInfo(broadcastType: AdminBroadcastType, broadcastMessage: String): Unit = {
    val message = s"[ADMIN: $name][SCOPE: ${broadcastType.routingKey}]: $broadcastMessage"
    channel.basicPublish(SPACE_EXCHANGE_NAME, broadcastType.routingKey, null, message.getBytes(SPACE_CHARSET))
    logger.info(s"Broadcasted: $broadcastMessage in scope: ${broadcastType.routingKey}")
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
        println(s"Provide next admin broadcast type: (${AdminBroadcastType.hint})")
        AdminBroadcastType.byName.get(StdIn.readLine()) match {
          case Some(broadcastType) =>
            println("Provide message:")
            admin.broadcastInfo(broadcastType, StdIn.readLine())
          case None => println("Invalid admin broadcast type.")
        }
      }

    }
  }

}

package space.actors

import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import com.typesafe.scalalogging.LazyLogging
import rabbitmq.RabbitConnectivity._
import space.messaging.SpaceInfo
import space.{SPACE_CHARSET, SPACE_EXCHANGE_NAME, _}
import util.SerializationUtils._

trait SpaceActor extends LazyLogging {

  implicit def channel: Channel

  def name: String

  def declareAndBindQueue(queueName: String, exchangeName: String, routingKeys: Iterable[String]): Unit = {
    channel.declareQueue(queueName)
    routingKeys
      .tapEach(channel.queueBind(queueName, exchangeName, _))
      .foreach(key => logger.info(s"Queue: $queueName bound to exchange: $exchangeName with key: $key"))
  }

}

trait SpaceInfoConsumer extends SpaceActor {

  def infoQueueName: String

  def infoQueueKeys: Seq[String]

  def infoConsumer(): DefaultConsumer = new DefaultConsumer(channel) {
    override def handleDelivery(consumerTag: String,
                                envelope: Envelope,
                                properties: AMQP.BasicProperties,
                                body: Array[Byte]): Unit =
      try {
        val info = fromJSON[SpaceInfo](new String(body, SPACE_CHARSET))
        channel.basicAck(envelope.getDeliveryTag, false)
        logger.info(s"Received info: $info")
      } catch {
        case ex: Exception => logger.warn(s"Failed to receive info: ${ex.getMessage}")
      }
  }

  def consumeInfo(): Unit = {
    declareAndBindQueue(infoQueueName, SPACE_EXCHANGE_NAME, infoQueueKeys)
    consumeFromQueue(infoQueueName, infoConsumer())
  }

  def consumeFromQueue(queueName: String, callback: => DefaultConsumer): Unit = {
    channel.basicConsume(queueName, false, callback)
    logger.info(s"Listening on queue: $queueName")
  }
}

trait SpaceInfoProducer extends SpaceActor {

  def produceInfo(spaceInfo: SpaceInfo): Unit = {
    val infoSerialized = spaceInfo.toJSON.getBytes(SPACE_CHARSET)
    channel.basicPublish(SPACE_EXCHANGE_NAME, spaceInfo.infoType.routingKey, null, infoSerialized)
    logger.info(s"Sent info: $spaceInfo")
  }

}

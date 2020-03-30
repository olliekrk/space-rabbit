package rabbitmq

import com.rabbitmq.client.{BuiltinExchangeType, Channel, Connection, ConnectionFactory}

object RabbitConnectivity {

  final val HOST_NAME = "127.0.0.1"

  private val connectionFactory = {
    val factory = new ConnectionFactory
    factory.setHost(HOST_NAME)
    factory
  }

  def createConnection: Connection = connectionFactory.newConnection()

  def sanitizeName(name: String): String = name.replaceAll("[^a-zA-Z]+","")

  implicit class ChannelDeclarations(channel: Channel) {

    def declareTopicExchange(exchangeName: String): Channel = {
      channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC)
      channel
    }

    def declareQueue(queueName: String): Channel = {
      channel.queueDeclare(queueName, false, false, true, null)
      channel
    }

  }

}

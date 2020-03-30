import java.nio.charset.Charset

package object space {

  final val SPACE_EXCHANGE_NAME = "spaceExchange"

  final val SPACE_CHARSET = Charset.forName("UTF-8")

  final val SPACE_ROUTING_KEY = "space"

  final val SPACE_AGENCY_ROUTING_KEY = s"$SPACE_ROUTING_KEY.agency"

  final val SPACE_CARRIER_ROUTING_KEY = s"$SPACE_ROUTING_KEY.carrier"

}

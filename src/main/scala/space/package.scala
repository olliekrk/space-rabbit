import java.nio.charset.Charset

import org.json4s.JsonAST.JString
import org.json4s.{CustomSerializer, DefaultFormats, Formats, Serializer}
import space.messaging.SpaceTaskType

package object space {

  final val SPACE_EXCHANGE_NAME = "spaceExchange"

  final val SPACE_CHARSET = Charset.forName("UTF-8")

  final val SPACE_ROUTING_KEY = "space"

  final val SPACE_AGENCY_ROUTING_KEY = s"$SPACE_ROUTING_KEY.agency"

  final val SPACE_CARRIER_ROUTING_KEY = s"$SPACE_ROUTING_KEY.carrier"

  implicit val formats: Formats = DefaultFormats ++ CustomSpaceSerializers.serializers

  object CustomSpaceSerializers {

    private val spaceTaskTypeSerializer = new CustomSerializer[SpaceTaskType]((_: Formats) => ( {
      case JString(name) => SpaceTaskType.byName(name)
    }, {
      case taskType: SpaceTaskType => JString(taskType.name)
    }
    ))

    val serializers: Seq[Serializer[_]] = Seq(
      spaceTaskTypeSerializer
    )
  }

}

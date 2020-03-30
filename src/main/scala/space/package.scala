import java.nio.charset.Charset

import org.json4s.JsonAST.{JField, JObject, JString}
import org.json4s.{CustomSerializer, DefaultFormats, Formats, Serializer}
import space.messaging.SpaceInfoType.TaskConfirmation
import space.messaging._

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

    private val spaceInfoTypeSerializer = new CustomSerializer[SpaceInfoType]((_: Formats) => ( {
      case JObject(JField("name", JString("confirmation")) :: JField("agencyName", JString(agencyName)) :: Nil) =>
        TaskConfirmation(agencyName)
      case JString(name) =>
        SpaceInfoType.byName(name)
    }, {
      case infoType: TaskConfirmation =>
        JObject(JField("name", JString(infoType.name)) :: JField("agencyName", JString(infoType.agencyName)) :: Nil)
      case infoType: SpaceInfoType =>
        JString(infoType.name)
    }
    ))

    val serializers: Seq[Serializer[_]] = Seq(
      spaceTaskTypeSerializer,
      spaceInfoTypeSerializer
    )
  }

}

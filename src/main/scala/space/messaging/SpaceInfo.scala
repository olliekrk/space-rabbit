package space.messaging

import space._

case class SpaceInfo(infoType: SpaceInfoType, infoMessage: String)

sealed abstract class SpaceInfoType(val routingKey: String)

object SpaceInfoType {

  case object SpaceBroadcast extends SpaceInfoType(SPACE_ROUTING_KEY)

  case object AgenciesBroadcast extends SpaceInfoType(SPACE_AGENCY_ROUTING_KEY)

  case object CarriersBroadcast extends SpaceInfoType(SPACE_CARRIER_ROUTING_KEY)

  case class TaskConfirmation(agencyName: String) extends SpaceInfoType(s"$SPACE_AGENCY_ROUTING_KEY.$agencyName")

  val byName: Map[String, SpaceInfoType] = Map(
    SpaceBroadcast.routingKey -> SpaceBroadcast,
    AgenciesBroadcast.routingKey -> AgenciesBroadcast,
    CarriersBroadcast.routingKey -> CarriersBroadcast,
  )

  def hint: String = byName.keys.mkString("|")

}

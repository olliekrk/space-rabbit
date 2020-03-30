package space.messaging

import space._

case class SpaceInfo(infoType: SpaceInfoType, infoMessage: String)

sealed abstract class SpaceInfoType(val name: String, val routingKey: String)

object SpaceInfoType {

  case object SpaceBroadcast extends SpaceInfoType("all", SPACE_ROUTING_KEY)

  case object AgenciesBroadcast extends SpaceInfoType("agencies", SPACE_AGENCY_ROUTING_KEY)

  case object CarriersBroadcast extends SpaceInfoType("carriers", SPACE_CARRIER_ROUTING_KEY)

  case class TaskConfirmation(agencyName: String) extends SpaceInfoType("confirmation", s"$SPACE_AGENCY_ROUTING_KEY.$agencyName")

  val byName: Map[String, SpaceInfoType] = Map(
    SpaceBroadcast.name -> SpaceBroadcast,
    AgenciesBroadcast.name -> AgenciesBroadcast,
    CarriersBroadcast.name -> CarriersBroadcast,
  )

  def hint: String = byName.keys.mkString("|")

}

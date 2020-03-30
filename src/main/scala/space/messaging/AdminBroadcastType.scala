package space.messaging

import space._

sealed abstract class AdminBroadcastType(val routingKey: String)

object AdminBroadcastType {

  case object SpaceBroadcast extends AdminBroadcastType(SPACE_ROUTING_KEY)

  case object AgenciesBroadcast extends AdminBroadcastType(SPACE_AGENCY_ROUTING_KEY)

  case object CarriersBroadcast extends AdminBroadcastType(SPACE_CARRIER_ROUTING_KEY)

  val byName: Map[String, AdminBroadcastType] = Map(
    SpaceBroadcast.routingKey -> SpaceBroadcast,
    AgenciesBroadcast.routingKey -> AgenciesBroadcast,
    CarriersBroadcast.routingKey -> CarriersBroadcast,
  )

  def hint: String = byName.keys.mkString("|")

}

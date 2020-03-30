package space.messaging

import space.SPACE_ROUTING_KEY

case class SpaceTask(taskType: SpaceTaskType, agencyName: String, taskId: String)

sealed abstract class SpaceTaskType(val name: String, val routingKey: String, val queueName: String)

object SpaceTaskType {

  case object TransitTask extends SpaceTaskType("transit", s"$SPACE_ROUTING_KEY.transit", "spaceTask_transit")

  case object CargoTask extends SpaceTaskType("cargo", s"$SPACE_ROUTING_KEY.cargo", "spaceTask_cargo")

  case object SatelliteTask extends SpaceTaskType("satellite", s"$SPACE_ROUTING_KEY.satellite", "spaceTask_satellite")

  val byName: Map[String, SpaceTaskType] = Map(
    TransitTask.name -> TransitTask,
    CargoTask.name -> CargoTask,
    SatelliteTask.name -> SatelliteTask
  )

  def hint: String = byName.keys.mkString("|")
}


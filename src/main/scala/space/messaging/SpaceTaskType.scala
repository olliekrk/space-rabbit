package space.messaging

sealed abstract class SpaceTaskType(val name: String, val routingKey: String, val queueName: String)

object SpaceTaskType {

  case object TransitTask extends SpaceTaskType("transit", "space.transit", "spaceTask_transit")

  case object CargoTask extends SpaceTaskType("cargo", "space.cargo", "spaceTask_cargo")

  case object SatelliteTask extends SpaceTaskType("satellite", "space.satellite", "spaceTask_satellite")

  val byName: Map[String, SpaceTaskType] = Map(
    TransitTask.name -> TransitTask,
    CargoTask.name -> CargoTask,
    SatelliteTask.name -> SatelliteTask
  )

  def hint: String = byName.keys.mkString("|")
}

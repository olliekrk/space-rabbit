package space.messaging

import java.util.UUID

case class SpaceTask(taskType: SpaceTaskType,
                     agencyName: String,
                     taskId: UUID = UUID.randomUUID())

package fr.rob.game.entity.notifier

import fr.rob.game.entity.guid.ObjectGuid

data class ScheduledItem(val delay: Int, val identifier: ObjectGuid, val function: () -> Unit)

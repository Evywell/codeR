package fr.rob.game.domain.entity.notifier

import fr.rob.game.domain.entity.guid.ObjectGuid

data class ScheduledItem(val delay: Int, val identifier: ObjectGuid, val function: () -> Unit)

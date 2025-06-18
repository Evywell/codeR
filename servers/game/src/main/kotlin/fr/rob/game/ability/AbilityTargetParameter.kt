package fr.rob.game.ability

import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid

class AbilityTargetParameter(
    private val targetGuid: ObjectGuid?,
    private val source: WorldObject,
)

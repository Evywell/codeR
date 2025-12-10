package fr.rob.game.domain.ability

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.guid.ObjectGuid

class AbilityTargetParameter(
    private val targetGuid: ObjectGuid?,
    private val source: WorldObject,
)

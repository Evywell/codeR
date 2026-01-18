package fr.rob.game.ability

import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid

class AbilityTargetParameter(
    private val targetGuid: ObjectGuid?,
    private val source: WorldObject,
) {
    fun getPrimaryTarget(): WorldObject? {
        if (targetGuid == null) {
            return null
        }

        return source.mapInstance.findObjectByGuid(targetGuid)
    }

    fun getSource(): WorldObject = source
}

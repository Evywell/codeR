package fr.rob.game.spell.target

import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.instance.MapInstance

class SpellTargetParameter(
    private val targetGuid: ObjectGuid?,
    private val targetingOnInstance: MapInstance,
) {
    fun getPrimaryTarget(): WorldObject? {
        if (targetGuid == null) {
            return null
        }

        return targetingOnInstance.findObjectByGuid(targetGuid)
    }
}

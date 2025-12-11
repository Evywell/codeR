package fr.rob.game.spell.target

import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.instance.MapInstance
import java.util.Optional

class SpellTargetParameter(
    private val targetGuid: ObjectGuid?,
    private val targetingOnInstance: MapInstance,
) {
    fun getPrimaryTarget(): Optional<WorldObject> {
        if (targetGuid == null) {
            return Optional.empty()
        }

        return targetingOnInstance.findObjectByGuid(targetGuid)
    }
}

package fr.rob.game.domain.spell.target

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.instance.MapInstance
import java.util.Optional

class SpellTargetParameter(
    private val targetGuid: ObjectGuid?,
    private val targetingOnInstance: MapInstance,
) {
    fun getPrimaryTarget(): Optional<WorldObject> {
        if (targetGuid == null) {
            return Optional.empty()
        }

        return targetingOnInstance.grid.findObjectByGuid(targetGuid)
    }
}

package fr.rob.game.domain.spell.target

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.instance.MapInstance
import java.util.Optional

class SingleObjectInInstanceSpellTarget(
    private val targetGuid: ObjectGuid,
    private val mapInstance: MapInstance,
) : SpellTargetInterface {
    override fun getTargets(): Array<WorldObject> {
        val target = getFirstTarget()

        if (target.isPresent) {
            return arrayOf(target.get())
        }

        return emptyArray()
    }

    override fun getFirstTarget(): Optional<WorldObject> = mapInstance.grid.findObjectByGuid(targetGuid)
}

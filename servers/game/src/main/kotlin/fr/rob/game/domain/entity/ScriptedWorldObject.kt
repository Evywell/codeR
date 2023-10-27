package fr.rob.game.domain.entity

import fr.rob.game.domain.entity.behavior.ScriptableTrait
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.script.ScriptInterface

class ScriptedWorldObject(guid: ObjectGuid) : WorldObject(guid) {
    init {
        addTrait(ScriptableTrait())
    }

    fun addScript(script: ScriptInterface) {
        getTrait<ScriptableTrait>().get().addScript(script)
    }
}

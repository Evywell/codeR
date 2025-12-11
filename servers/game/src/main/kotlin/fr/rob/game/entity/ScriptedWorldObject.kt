package fr.rob.game.entity

import fr.rob.game.entity.behavior.ScriptableTrait
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.script.ScriptInterface

class ScriptedWorldObject(guid: ObjectGuid) : WorldObject(guid) {
    init {
        addTrait(ScriptableTrait())
    }

    fun addScript(script: ScriptInterface) {
        getTrait<ScriptableTrait>().get().addScript(script)
    }
}

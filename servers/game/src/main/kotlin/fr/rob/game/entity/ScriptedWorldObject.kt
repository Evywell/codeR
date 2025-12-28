package fr.rob.game.entity

import fr.rob.game.behavior.script.ScriptableBehavior
import fr.rob.game.component.ScriptableComponent
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.script.ScriptInterface

class ScriptedWorldObject(guid: ObjectGuid) : WorldObject(guid) {
    init {
        addComponent(ScriptableComponent())
        addBehavior(ScriptableBehavior)
    }

    fun addScript(script: ScriptInterface) {
        getComponent<ScriptableComponent>()?.addScript(script)
    }
}

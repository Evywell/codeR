package fr.rob.game.entity.behavior

import fr.rob.game.entity.UpdatableTraitInterface
import fr.rob.game.script.ScriptInterface

class ScriptableTrait : UpdatableTraitInterface {
    private val scripts = ArrayList<ScriptInterface>()

    fun addScript(script: ScriptInterface) {
        scripts.add(script)
    }

    override fun update(deltaTime: Int) {
        scripts.forEach { it.update(deltaTime) }
    }
}

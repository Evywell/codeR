package fr.rob.game.domain.entity.behavior

import fr.rob.game.domain.entity.UpdatableTraitInterface
import fr.rob.game.domain.script.ScriptInterface

class ScriptableTrait : UpdatableTraitInterface {
    private val scripts = ArrayList<ScriptInterface>()

    fun addScript(script: ScriptInterface) {
        scripts.add(script)
    }

    override fun update(deltaTime: Int) {
        scripts.forEach { it.update(deltaTime) }
    }
}

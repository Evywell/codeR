package fr.rob.game.component

import fr.rob.game.script.ScriptInterface

data class ScriptableComponent(val scripts: MutableList<ScriptInterface> = mutableListOf()) {
    fun addScript(script: ScriptInterface) {
        scripts.add(script)
    }
}
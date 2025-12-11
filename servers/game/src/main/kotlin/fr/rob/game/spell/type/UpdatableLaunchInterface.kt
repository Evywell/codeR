package fr.rob.game.spell.type

import fr.rob.game.spell.Spell

interface UpdatableLaunchInterface {
    fun update(deltaTime: Int, spell: Spell)
}

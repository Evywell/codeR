package fr.rob.game.domain.spell.type

import fr.rob.game.domain.spell.Spell

interface UpdatableLaunchInterface {
    fun update(deltaTime: Int, spell: Spell)
}

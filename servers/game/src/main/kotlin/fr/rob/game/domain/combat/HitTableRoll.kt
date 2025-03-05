package fr.rob.game.domain.combat

import fr.rob.game.domain.entity.WorldObject

data class HitTableRoll(val attacker: WorldObject, val victim: WorldObject, private var roll: Int) {
    fun applyStepChance(stepChance: Int): Boolean {
        val previousRollValue = roll
        roll -= stepChance

        return previousRollValue <= stepChance
    }

    fun getRoll() = roll
}

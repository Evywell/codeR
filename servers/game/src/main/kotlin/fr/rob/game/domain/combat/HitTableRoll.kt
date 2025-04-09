package fr.rob.game.domain.combat

import fr.rob.game.domain.entity.WorldObject

data class HitTableRoll(
    val attacker: WorldObject,
    val victim: WorldObject,
    private var roll: Int,
) {
    fun isSucceed(stepChance: Int): Boolean = roll <= stepChance

    fun exhaustStepChance(stepChance: Int) {
        roll -= stepChance
    }

    fun getRoll() = roll
}

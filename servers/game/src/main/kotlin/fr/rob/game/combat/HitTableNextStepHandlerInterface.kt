package fr.rob.game.combat

import fr.rob.game.combat.hittable.MeleeHitTableStepInterface

interface HitTableNextStepHandlerInterface {
    fun getHitTableStep(): MeleeHitTableStepInterface

    fun executeNextStep(
        hitRoll: HitTableRoll,
        exhaustedCurrentStepChance: Int,
    ): MeleeHitTableResult
}

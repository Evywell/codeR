package fr.rob.game.combat.hittable

import fr.rob.game.combat.HitTableNextStepHandlerInterface
import fr.rob.game.combat.HitTableRoll
import fr.rob.game.combat.MeleeHitTableResult

class CriticalHitMeleeStep : MeleeHitTableStepInterface {
    override fun execute(
        hitRoll: HitTableRoll,
        nextStepHandler: HitTableNextStepHandlerInterface
    ): MeleeHitTableResult {
        if (hitRoll.isSucceed(CRITICAL_HIT_BASE_CHANCE)) {
            return MeleeHitTableResult.CRITICAL_HIT
        }

        return nextStepHandler.executeNextStep(hitRoll, CRITICAL_HIT_BASE_CHANCE)
    }

    companion object {
        private const val CRITICAL_HIT_BASE_CHANCE = 100 // 1%
    }
}

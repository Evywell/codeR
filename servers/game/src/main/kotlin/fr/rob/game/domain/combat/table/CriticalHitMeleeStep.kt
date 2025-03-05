package fr.rob.game.domain.combat.table

import fr.rob.game.domain.combat.HitTableNextStepHandlerInterface
import fr.rob.game.domain.combat.HitTableRoll
import fr.rob.game.domain.combat.MeleeHitTableResult

class CriticalHitMeleeStep : MeleeHitTableStepInterface {
    override fun execute(
        hitRoll: HitTableRoll,
        nextStepHandler: HitTableNextStepHandlerInterface
    ): MeleeHitTableResult {
        if (hitRoll.applyStepChance(CRITICAL_HIT_BASE_CHANCE)) {
            return MeleeHitTableResult.CRITICAL_HIT
        }

        return nextStepHandler.getHitTableStep().execute(hitRoll, nextStepHandler)
    }

    companion object {
        private const val CRITICAL_HIT_BASE_CHANCE = 100 // 1%
    }
}

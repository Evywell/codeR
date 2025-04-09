package fr.rob.game.domain.combat.table

import fr.rob.game.domain.combat.HitTableNextStepHandlerInterface
import fr.rob.game.domain.combat.HitTableRoll
import fr.rob.game.domain.combat.MeleeHitTableResult

class MissMeleeStep : MeleeHitTableStepInterface {
    override fun execute(
        hitRoll: HitTableRoll,
        nextStepHandler: HitTableNextStepHandlerInterface
    ): MeleeHitTableResult {
        if (hitRoll.isSucceed(MISS_CHANCE)) {
            return MeleeHitTableResult.MISS
        }

        return nextStepHandler.executeNextStep(hitRoll, MISS_CHANCE)
    }

    companion object {
        private const val MISS_CHANCE = 500 // 5%
    }
}

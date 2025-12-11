package fr.rob.game.combat.hittable

import fr.rob.game.combat.HitTableNextStepHandlerInterface
import fr.rob.game.combat.HitTableRoll
import fr.rob.game.combat.MeleeHitTableResult

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

package fr.rob.game.domain.combat.table

import fr.rob.game.domain.combat.HitTableNextStepHandlerInterface
import fr.rob.game.domain.combat.HitTableRoll
import fr.rob.game.domain.combat.MeleeHitTableResult

class MissMeleeStep : MeleeHitTableStepInterface {
    override fun execute(
        hitRoll: HitTableRoll,
        nextStepHandler: HitTableNextStepHandlerInterface
    ): MeleeHitTableResult {
        if (hitRoll.applyStepChance(MISS_CHANCE)) {
            return MeleeHitTableResult.MISS
        }

        return nextStepHandler.getHitTableStep().execute(hitRoll, nextStepHandler)
    }

    companion object {
        private const val MISS_CHANCE = 500 // 5%
    }
}

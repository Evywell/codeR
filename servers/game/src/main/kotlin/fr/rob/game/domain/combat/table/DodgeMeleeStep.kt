package fr.rob.game.domain.combat.table

import fr.rob.game.domain.combat.HitTableNextStepHandlerInterface
import fr.rob.game.domain.combat.HitTableRoll
import fr.rob.game.domain.combat.MeleeHitTableResult

class DodgeMeleeStep : MeleeHitTableStepInterface {
    override fun execute(
        hitRoll: HitTableRoll,
        nextStepHandler: HitTableNextStepHandlerInterface
    ): MeleeHitTableResult {
        if (hitRoll.applyStepChance(DODGE_BASE_CHANCE)) {
            return MeleeHitTableResult.DODGE
        }

        return nextStepHandler.getHitTableStep().execute(hitRoll, nextStepHandler)
    }

    companion object {
        private const val DODGE_BASE_CHANCE = 500 // 5%
    }
}

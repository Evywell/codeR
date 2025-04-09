package fr.rob.game.domain.combat.table

import fr.rob.game.domain.combat.HitTableNextStepHandlerInterface
import fr.rob.game.domain.combat.HitTableRoll
import fr.rob.game.domain.combat.MeleeHitTableResult

class DodgeMeleeStep : MeleeHitTableStepInterface {
    override fun execute(
        hitRoll: HitTableRoll,
        nextStepHandler: HitTableNextStepHandlerInterface
    ): MeleeHitTableResult {
        if (hitRoll.attacker.isBehindOf(hitRoll.victim)) {
            return nextStepHandler.executeNextStep(hitRoll, 0)
        }

        if (hitRoll.isSucceed(DODGE_BASE_CHANCE)) {
            return MeleeHitTableResult.DODGE
        }

        return nextStepHandler.executeNextStep(hitRoll, DODGE_BASE_CHANCE)
    }

    companion object {
        private const val DODGE_BASE_CHANCE = 500 // 5%
    }
}

package fr.rob.game.domain.combat.table

import fr.rob.game.domain.combat.HitTableNextStepHandlerInterface
import fr.rob.game.domain.combat.HitTableRoll
import fr.rob.game.domain.combat.MeleeHitTableResult

class CrushingBlowStep : MeleeHitTableStepInterface {
    override fun execute(
        hitRoll: HitTableRoll,
        nextStepHandler: HitTableNextStepHandlerInterface
    ): MeleeHitTableResult {
        // Not applicable when attacker is a player
        if (hitRoll.attacker.guid.isPlayer()) {
            return nextStepHandler.executeNextStep(hitRoll, 0)
        }

        if (hitRoll.isSucceed(CRUSHING_BLOW_BASE_CHANCE)) {
            return MeleeHitTableResult.CRUSHING_BLOW
        }

        return nextStepHandler.executeNextStep(hitRoll, CRUSHING_BLOW_BASE_CHANCE)
    }

    companion object {
        private const val CRUSHING_BLOW_BASE_CHANCE = 10000 // 10%
    }
}

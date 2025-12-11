package fr.rob.game.combat.hittable

import fr.rob.game.combat.HitTableNextStepHandlerInterface
import fr.rob.game.combat.HitTableRoll
import fr.rob.game.combat.MeleeHitTableResult

class HitMeleeStep : MeleeHitTableStepInterface {
    override fun execute(
        hitRoll: HitTableRoll,
        nextStepHandler: HitTableNextStepHandlerInterface
    ): MeleeHitTableResult = MeleeHitTableResult.HIT
}

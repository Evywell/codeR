package fr.rob.game.test.unit.tools

import fr.rob.game.combat.HitTableNextStepHandlerInterface
import fr.rob.game.combat.HitTableRoll
import fr.rob.game.combat.MeleeHitTableResult
import fr.rob.game.combat.hittable.MeleeHitTableStepInterface
import fr.rob.game.test.unit.domain.combat.table.EndOfHitTableException

class HitTableStepTester : HitTableNextStepHandlerInterface {
    override fun getHitTableStep(): MeleeHitTableStepInterface {
        throw EndOfHitTableException()
    }

    override fun executeNextStep(hitRoll: HitTableRoll, exhaustedCurrentStepChance: Int): MeleeHitTableResult = getHitTableStep().execute(hitRoll, this)
}

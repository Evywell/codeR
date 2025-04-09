package fr.rob.game.test.unit.tools

import fr.rob.game.domain.combat.HitTableNextStepHandlerInterface
import fr.rob.game.domain.combat.HitTableRoll
import fr.rob.game.domain.combat.MeleeHitTableResult
import fr.rob.game.domain.combat.table.MeleeHitTableStepInterface
import fr.rob.game.test.unit.domain.combat.table.EndOfHitTableException

class HitTableStepTester : HitTableNextStepHandlerInterface {
    override fun getHitTableStep(): MeleeHitTableStepInterface {
        throw EndOfHitTableException()
    }

    override fun executeNextStep(hitRoll: HitTableRoll, exhaustedCurrentStepChance: Int): MeleeHitTableResult = getHitTableStep().execute(hitRoll, this)
}

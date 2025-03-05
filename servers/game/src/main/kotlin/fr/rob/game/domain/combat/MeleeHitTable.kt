package fr.rob.game.domain.combat

import fr.rob.game.domain.combat.table.CriticalHitMeleeStep
import fr.rob.game.domain.combat.table.CrushingBlowStep
import fr.rob.game.domain.combat.table.DodgeMeleeStep
import fr.rob.game.domain.combat.table.HitMeleeStep
import fr.rob.game.domain.combat.table.MissMeleeStep
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.world.RollEngineInterface

class MeleeHitTable(private val rollEngine: RollEngineInterface) {
    fun rollMeleeHit(attacker: WorldObject, victim: WorldObject): MeleeHitTableResult {
        val steps = arrayOf(
            MissMeleeStep(),
            DodgeMeleeStep(),
            CriticalHitMeleeStep(),
            CrushingBlowStep(),
            HitMeleeStep()
        )
        val stepHandler = HitTableNextStepHandler(steps)
        val roll = rollEngine.roll(0, 10000)

        return stepHandler.getHitTableStep().execute(HitTableRoll(attacker, victim, roll), stepHandler)
    }
}

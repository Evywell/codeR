package fr.rob.game.combat

import fr.rob.game.combat.hittable.CriticalHitMeleeStep
import fr.rob.game.combat.hittable.CrushingBlowStep
import fr.rob.game.combat.hittable.DodgeMeleeStep
import fr.rob.game.combat.hittable.HitMeleeStep
import fr.rob.game.combat.hittable.MissMeleeStep
import fr.rob.game.entity.WorldObject
import fr.rob.game.world.RollEngineInterface

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

package fr.rob.game.domain.combat

import fr.rob.game.domain.combat.table.MeleeHitTableStepInterface

class HitTableNextStepHandler(private val steps: Array<MeleeHitTableStepInterface>) : HitTableNextStepHandlerInterface {
    private var currentStep = 0

    override fun getHitTableStep(): MeleeHitTableStepInterface {
        if (currentStep > steps.size - 1) {
            throw RuntimeException("Hit table overflow")
        }

        val step = steps[currentStep]
        currentStep++

        return step
    }
}

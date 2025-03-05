package fr.rob.game.domain.combat

import fr.rob.game.domain.combat.table.MeleeHitTableStepInterface

interface HitTableNextStepHandlerInterface {
    fun getHitTableStep(): MeleeHitTableStepInterface
}

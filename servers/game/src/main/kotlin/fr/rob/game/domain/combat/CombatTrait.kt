package fr.rob.game.domain.combat

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.domain.entity.UpdatableTraitInterface
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.behavior.ObjectSheetTrait

class CombatTrait(private val source: WorldObject): UpdatableTraitInterface {
    private val mainHandAttacksInterval = IntervalTimer(MAIN_HAND_WEAPON_SPEED_MS, MAIN_HAND_WEAPON_SPEED_MS)
    private var currentTarget: WorldObject? = null

    fun engageCombatWith(target: WorldObject) {
        currentTarget = target
    }

    override fun update(deltaTime: Int) {
        if (!isInCombatWithTarget()) {
            return
        }

        mainHandAttacksInterval.update(deltaTime)

        if (!mainHandAttacksInterval.passed()) {
            return
        }

        mainHandAttacksInterval.reset()
        currentTarget?.getTrait<ObjectSheetTrait>()?.ifPresent {
            it.applySingleDamage(DamageSource(source, MAIN_HAND_WEAPON_DAMAGE_AMOUNT), it.isCriticalHit(source))
        }
    }

    private fun isInCombatWithTarget(): Boolean = currentTarget != null

    companion object {
        private const val MAIN_HAND_WEAPON_SPEED_MS = 1400
        private const val MAIN_HAND_WEAPON_DAMAGE_AMOUNT = 18
    }
}
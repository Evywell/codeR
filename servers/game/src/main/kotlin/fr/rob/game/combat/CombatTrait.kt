package fr.rob.game.combat

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.behavior.ObjectSheetBehavior
import fr.rob.game.player.message.DebugSignalMessage
import fr.rob.game.entity.UpdatableTraitInterface
import fr.rob.game.entity.WorldObject

class CombatTrait(private val source: WorldObject) : UpdatableTraitInterface {
    private val mainHandAttacksInterval = IntervalTimer(MAIN_HAND_WEAPON_SPEED_MS)
    private var currentTarget: WorldObject? = null
    private var shouldPerformAttack = true

    fun engageCombatWith(target: WorldObject) {
        currentTarget = target
    }

    override fun update(deltaTime: Int) {
        if (!isInCombatWithTarget()) {
            return
        }

        source.controlledByGameSession?.send(
            DebugSignalMessage(
                "IS_IN_FRONT_OF",
                if (source.isInFrontOf(currentTarget!!)) 1 else 0
            )
        )

        if (shouldPerformAttack) {
            performAttack()

            return
        }

        mainHandAttacksInterval.update(deltaTime)

        if (!mainHandAttacksInterval.passed()) {
            return
        }

        mainHandAttacksInterval.reset()
        shouldPerformAttack = true
    }

    private fun performAttack() {
        if (!source.isInMeleeRangeOf(currentTarget!!) || !source.isInFrontOf(currentTarget!!)) {
            return
        }

        val objectSheetBehavior = currentTarget?.getBehavior<ObjectSheetBehavior>() ?: return

        objectSheetBehavior.applySingleDamage(
            currentTarget!!,
            DamageSource(source, MAIN_HAND_WEAPON_DAMAGE_AMOUNT),
            objectSheetBehavior.isCriticalHit(source)
        )

        shouldPerformAttack = false
    }

    private fun isInCombatWithTarget(): Boolean = currentTarget != null

    companion object {
        private const val MAIN_HAND_WEAPON_SPEED_MS = 1400
        private const val MAIN_HAND_WEAPON_DAMAGE_AMOUNT = 18
    }
}

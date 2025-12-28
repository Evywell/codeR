package fr.rob.game.component

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.entity.WorldObject

class CombatComponent {
    var mainTarget: WorldObject? = null
        private set

    private val mainHandAttacksInterval = IntervalTimer(MAIN_HAND_WEAPON_SPEED_MS)

    fun engageCombatWith(target: WorldObject) {
        mainTarget = target
    }

    fun isInCombatWithMainTarget(): Boolean = mainTarget != null

    fun isWeaponSwingReady(timeElapsed: Int): Boolean {
        mainHandAttacksInterval.update(timeElapsed)

        return mainHandAttacksInterval.passed()
    }

    fun weaponSwing() {
        mainHandAttacksInterval.reset()
    }

    companion object {
        private const val MAIN_HAND_WEAPON_SPEED_MS = 1400
    }
}
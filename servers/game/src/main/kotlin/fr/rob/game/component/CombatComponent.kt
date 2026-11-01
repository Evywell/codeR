package fr.rob.game.component

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid

class CombatComponent {
    var mainTargetGuid: ObjectGuid? = null
        private set

    private val mainHandAttacksInterval = IntervalTimer(MAIN_HAND_WEAPON_SPEED_MS)

    fun engageCombatWith(target: WorldObject) {
        mainTargetGuid = target.guid
    }

    fun disengage() {
        mainTargetGuid = null
    }

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

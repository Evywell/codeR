package fr.rob.game.component

import fr.rob.game.entity.guid.ObjectGuid

class CombatComponent {
    var mainTargetGuid: ObjectGuid? = null
        internal set

    companion object {
        const val MAIN_HAND_WEAPON_SPEED_MS = 1400
    }
}

package fr.rob.game.spell

import fr.rob.game.spell.type.LaunchInfoInterface
import java.util.EnumSet

/**
 * @param castingTime Casting time of the spell in milliseconds (ms)
 */
class SpellInfo(
    val identifier: Int,
    val launchInfo: LaunchInfoInterface,
    val castingTime: Int = INSTANT_CASTING_TIME,
    val flags: EnumSet<FLAGS> = EnumSet.noneOf(FLAGS::class.java),
) {
    enum class FLAGS {
        ALLOW_CAST_WHILE_MOVING,
    }

    companion object {
        const val INSTANT_CASTING_TIME = 0
        const val CASTING_TIME_2_SECONDS = 2000
    }
}

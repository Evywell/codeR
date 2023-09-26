package fr.rob.game.domain.spell

import fr.rob.game.domain.spell.effect.SpellEffectInfo
import java.util.EnumSet

/**
 * @param castingTime Casting time of the spell in milliseconds (ms)
 */
class SpellInfo(
    val launchingType: LaunchType,
    val effects: Array<SpellEffectInfo>,
    val castingTime: Int = INSTANT_CASTING_TIME,
    val projectileSpeed: Float = 0f,
    val flags: EnumSet<FLAGS> = EnumSet.noneOf(FLAGS::class.java),
) {
    enum class LaunchType {
        INSTANT,
        GHOST_PROJECTILES,
        TIMED_PROJECTILES,
    }

    enum class FLAGS {
        ALLOW_CAST_WHILE_MOVING,
    }

    companion object {
        const val INSTANT_CASTING_TIME = 0
        const val CASTING_TIME_2_SECONDS = 2000
    }
}

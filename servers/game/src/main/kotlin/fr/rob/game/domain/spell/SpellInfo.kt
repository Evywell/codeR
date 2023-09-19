package fr.rob.game.domain.spell

import fr.rob.game.domain.spell.effect.SpellEffectInfo

data class SpellInfo(
    val launchingType: LaunchType,
    val effects: Array<SpellEffectInfo>,
    val projectileSpeed: Float = 0f,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpellInfo

        return effects.contentEquals(other.effects)
    }

    override fun hashCode(): Int {
        return effects.contentHashCode()
    }

    enum class LaunchType {
        INSTANT,
        GHOST_PROJECTILES,
        TIMED_PROJECTILES,
    }
}

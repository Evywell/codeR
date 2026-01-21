package fr.rob.game.ability

import fr.rob.game.ability.effect.AbilityEffectInfoInterface
import java.util.EnumSet

data class AbilityInfo(
    val identifier: Int,
    val type: AbilityType,
    val abilityRequirement: AbilityRequirements,
    val castingTimeMs: Int,
    val launchType: LaunchType,
    val effectsInfo: List<AbilityEffectInfoInterface> = emptyList(),
    val flags: EnumSet<Flags> = EnumSet.noneOf(Flags::class.java),
) {
    enum class Flags {
        ALLOW_CAST_WHILE_MOVING,
    }

    enum class LaunchType {
        INSTANT,
        TIMED_PROJECTILE,
        TRACKED_PROJECTILE,
    }

    companion object {
        const val INSTANT_CASTING_TIME = 0
    }
}

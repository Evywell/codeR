package fr.rob.game.ability

import fr.rob.game.ability.launch.LaunchInfoInterface
import java.util.EnumSet

data class AbilityInfo(
    val identifier: Int,
    val type: AbilityType,
    val abilityRequirement: AbilityRequirements,
    val launchInfo: LaunchInfoInterface,
    val castingTimeMs: Int,
    val flags: EnumSet<FLAGS> = EnumSet.noneOf(FLAGS::class.java),
) {
    enum class FLAGS {
        ALLOW_CAST_WHILE_MOVING,
    }

    companion object {
        const val INSTANT_CASTING_TIME = 0
    }
}

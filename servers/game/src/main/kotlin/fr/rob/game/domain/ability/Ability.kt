package fr.rob.game.domain.ability

import fr.rob.game.domain.entity.WorldObject

class Ability(
    private val info: AbilityInfo,
    private val source: WorldObject,
    private val target: AbilityTargetParameter,
) {
    private val abilityLauncher = info.launchInfo.createAbilityLauncher()

    fun use() {
    }
}

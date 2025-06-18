package fr.rob.game.ability

import fr.rob.game.entity.WorldObject

class Ability(
    private val info: AbilityInfo,
    private val source: WorldObject,
    private val target: AbilityTargetParameter,
) {
    private val abilityLauncher = info.launchInfo.createAbilityLauncher()

    fun use() {
    }
}

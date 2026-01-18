package fr.rob.game.ability

import fr.rob.game.behavior.BehaviorInterface
import fr.rob.game.entity.WorldObject

class AbilityBehavior(
    private val abilityManager: ObjectAbilityManager,
) : BehaviorInterface {

    override fun update(worldObject: WorldObject, deltaTime: Int) {
        val ongoingAbilities = worldObject.getOngoingAbilities()

        ongoingAbilities.removeIf { ability ->
            abilityManager.updateAbility(ability, deltaTime)
            ability.isDone()
        }
    }

    override fun canApplyTo(worldObject: WorldObject): Boolean = true
}

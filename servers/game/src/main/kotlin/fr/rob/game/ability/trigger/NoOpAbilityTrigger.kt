package fr.rob.game.ability.trigger

import fr.rob.game.ability.Ability

class NoOpAbilityTrigger : AbilityTriggerInterface {
    override fun trigger(ability: Ability) {
        // No operation - used for abilities with no effects
    }
}

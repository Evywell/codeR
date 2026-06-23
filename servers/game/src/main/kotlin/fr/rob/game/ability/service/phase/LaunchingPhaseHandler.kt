package fr.rob.game.ability.service.phase

import fr.rob.game.ability.Ability
import fr.rob.game.ability.launch.UpdatableLaunchTypeInterface

class LaunchingPhaseHandler : AbilityPhaseHandlerInterface {
    override fun canHandle(ability: Ability): Boolean =
        ability.state == Ability.AbilityState.LAUNCHING

    override fun handle(ability: Ability, deltaTime: Int) {
        if (ability.launchType == null) {
            ability.launchType = ability.info.launchType.createLauncher()
        }

        handleAbilityLaunch(ability, deltaTime)
    }

    private fun handleAbilityLaunch(ability: Ability, deltaTime: Int) {
        val abilityLauncher = ability.launchType ?: return
        abilityLauncher.handleLaunch()

        if (abilityLauncher is UpdatableLaunchTypeInterface) {
            abilityLauncher.update(deltaTime, ability)
        }

        if (abilityLauncher.isLaunchingCompleted()) {
            ability.setState(Ability.AbilityState.RESOLVING)
        }
    }
}

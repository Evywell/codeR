package fr.rob.game.ability.service.phase

import fr.rob.game.ability.Ability
import fr.rob.game.ability.launch.LaunchInfoInterface
import fr.rob.game.ability.launch.UpdatableLaunchTypeInterface

class LaunchingPhaseHandler(private val launchersInfo: List<LaunchInfoInterface>) : AbilityPhaseHandlerInterface {
    override fun canHandle(ability: Ability): Boolean =
        ability.state == Ability.AbilityState.LAUNCHING

    override fun handle(ability: Ability, deltaTime: Int) {
        if (ability.launchType != null) {
            handleAbilityLaunch(ability, deltaTime);

            return
        }

        val launcherInfo = launchersInfo.firstOrNull { it.support(ability) } ?: return
        ability.launchType = launcherInfo.createAbilityLauncher(ability)

        handleAbilityLaunch(ability, deltaTime)
    }

    private fun handleAbilityLaunch(ability: Ability, deltaTime: Int) {
        val abilityLauncher = ability.launchType ?: return
        abilityLauncher.handleLaunch()

        if (abilityLauncher is UpdatableLaunchTypeInterface) {
            abilityLauncher.update(deltaTime, ability)
        }

        if (abilityLauncher.isLaunchingCompleted()) {
            ability.state = Ability.AbilityState.RESOLVING
        }
    }
}

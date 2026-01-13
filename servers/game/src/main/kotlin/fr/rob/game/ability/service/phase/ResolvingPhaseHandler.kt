package fr.rob.game.ability.service.phase

import fr.rob.game.ability.Ability
import fr.rob.game.ability.launch.UpdatableLaunchTypeInterface

class ResolvingPhaseHandler : AbilityPhaseHandlerInterface {

    override fun canHandle(ability: Ability): Boolean =
        ability.state == Ability.AbilityState.RESOLVING

    override fun handle(ability: Ability, deltaTime: Int) {
        val launcher = ability.getLauncher()

        launcher.handleLaunch()

        if (launcher is UpdatableLaunchTypeInterface) {
            launcher.update(deltaTime, ability)
        }

        if (launcher.isLaunchingCompleted()) {
            ability.state = Ability.AbilityState.DONE
        }
    }
}

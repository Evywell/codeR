package fr.rob.game.domain.ability.launch

class InstantLaunchInfo : LaunchInfoInterface {
    override fun createAbilityLauncher(): LaunchTypeInterface = InstantLaunchType()
}

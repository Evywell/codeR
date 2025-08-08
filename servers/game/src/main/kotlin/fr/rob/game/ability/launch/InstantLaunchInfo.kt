package fr.rob.game.ability.launch

class InstantLaunchInfo : LaunchInfoInterface {
    override fun createAbilityLauncher(): LaunchTypeInterface = InstantLaunchType()
}

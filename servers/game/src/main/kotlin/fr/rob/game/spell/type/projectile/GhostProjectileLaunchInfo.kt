package fr.rob.game.spell.type.projectile

import fr.rob.game.spell.trigger.SpellTriggerInterface
import fr.rob.game.spell.type.LaunchInfoInterface
import fr.rob.game.spell.type.LaunchTypeInterface

class GhostProjectileLaunchInfo(
    val projectileSpeed: Float,
    val onHitTargetTrigger: SpellTriggerInterface
) : LaunchInfoInterface {
    override fun createSpellLauncher(): LaunchTypeInterface = GhostProjectileLaunchType(this)
}

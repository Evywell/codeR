package fr.rob.game.domain.spell.type.projectile

import fr.rob.game.domain.spell.trigger.SpellTriggerInterface
import fr.rob.game.domain.spell.type.LaunchInfoInterface
import fr.rob.game.domain.spell.type.LaunchTypeInterface

class GhostProjectileLaunchInfo(
    val projectileSpeed: Float,
    val onHitTargetTrigger: SpellTriggerInterface
) : LaunchInfoInterface {
    override fun createSpellLauncher(): LaunchTypeInterface = GhostProjectileLaunchType(this)
}

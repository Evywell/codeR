package fr.rob.game.ability.launch

import fr.rob.game.ability.launch.instant.InstantLaunchType
import fr.rob.game.ability.launch.projectile.FixedTimeProjectileParameters
import fr.rob.game.ability.launch.projectile.FixedTimeProjectileLaunchType
import fr.rob.game.ability.launch.projectile.GhostProjectileLaunchType

sealed class LaunchType {
    abstract fun createLauncher(): LaunchTypeInterface

    object Instant : LaunchType() {
        override fun createLauncher() = InstantLaunchType()
    }

    data class FixedTimeProjectile(val parameters: FixedTimeProjectileParameters) : LaunchType() {
        override fun createLauncher() = FixedTimeProjectileLaunchType(parameters)
    }

    data class TrackedProjectile(val projectileSpeed: Float) : LaunchType() {
        override fun createLauncher() = GhostProjectileLaunchType(projectileSpeed)
    }
}

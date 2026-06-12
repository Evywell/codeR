package fr.rob.game.ability.launch

data class LaunchType(val typeName: Name, val parameters: LaunchParametersInterface? = null) {
    enum class Name {
        INSTANT,
        FIXED_TIME_PROJECTILE,
        TRACKED_PROJECTILE,
    }
}
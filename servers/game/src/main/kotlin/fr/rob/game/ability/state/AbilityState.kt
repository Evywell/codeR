package fr.rob.game.ability.state

sealed class AbilityState {
    object CheckingRequirements : AbilityState()

    object ResolvingAbility : AbilityState()

    object NotInitialized : AbilityState()

    object PerformingAbility : AbilityState()

    object Launching : AbilityState()

    object Done : AbilityState()

    object Failed : AbilityState()
}

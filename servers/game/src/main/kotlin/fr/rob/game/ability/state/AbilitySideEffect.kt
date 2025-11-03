package fr.rob.game.ability.state

sealed class AbilitySideEffect {
    object CheckRequirements : AbilitySideEffect()

    object HandleCasting : AbilitySideEffect()

    object ComputeResources : AbilitySideEffect()

    object ComputeEffects : AbilitySideEffect()

    object HandleLaunching : AbilitySideEffect()
}

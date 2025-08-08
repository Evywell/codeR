package fr.rob.game.ability.state

sealed class AbilityEvent {
    object OnUseAbility : AbilityEvent()

    object OnPerformingAbility : AbilityEvent()

    object OnResolvingAbility : AbilityEvent()

    object OnAbilityResolved : AbilityEvent()

    object OnCheckingRequirementsFails : AbilityEvent()

    object OnResourcesComputed : AbilityEvent()
}

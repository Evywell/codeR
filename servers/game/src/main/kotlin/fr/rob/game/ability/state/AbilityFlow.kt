package fr.rob.game.ability.state

import com.tinder.StateMachine
import fr.rob.game.ability.AbilityInfo
import fr.rob.game.entity.WorldObject

class AbilityFlow(
    private val source: WorldObject,
    private val abilityInfo: AbilityInfo,
) {
    private val abilityStateMachine =
        StateMachine.create<AbilityState, AbilityEvent, AbilitySideEffect> {
            initialState(AbilityState.NotInitialized)

            state<AbilityState.NotInitialized> {
                on<AbilityEvent.OnUseAbility> {
                    transitionTo(AbilityState.CheckingRequirements, AbilitySideEffect.CheckRequirements)
                }
            }

            state<AbilityState.CheckingRequirements> {
                on<AbilityEvent.OnPerformingAbility> {
                    transitionTo(AbilityState.PerformingAbility, AbilitySideEffect.HandleCasting)
                }

                on<AbilityEvent.OnCheckingRequirementsFails> {
                    transitionTo(AbilityState.Failed)
                }
            }

            state<AbilityState.PerformingAbility> {
                on<AbilityEvent.OnResolvingAbility> {
                    transitionTo(AbilityState.ResolvingAbility, AbilitySideEffect.ComputeResources)
                }
            }

            state<AbilityState.ResolvingAbility> {
                on<AbilityEvent.OnResourcesComputed> {
                    dontTransition(AbilitySideEffect.ComputeEffects)
                }

                on<AbilityEvent.OnAbilityResolved> {
                    transitionTo(AbilityState.Done)
                }
            }

            state<AbilityState.Failed> {}
            state<AbilityState.Done> {}

            onTransition {
                val validTransition = it as? StateMachine.Transition.Valid ?: return@onTransition

                when (validTransition.sideEffect) {
                    AbilitySideEffect.CheckRequirements -> checkRequirements()
                    AbilitySideEffect.HandleCasting -> handleCasting()
                    AbilitySideEffect.ComputeResources -> computeResources()
                    AbilitySideEffect.ComputeEffects -> computeEffects()
                    null -> {}
                }
            }
        }

    fun useAbility() {
        abilityStateMachine.transition(AbilityEvent.OnUseAbility)
    }

    fun resumeCasting(isCastingSequenceDone: Boolean) {
        if (abilityInfo.castingTimeMs != AbilityInfo.INSTANT_CASTING_TIME && !isCastingSequenceDone) {
            return
        }

        abilityStateMachine.transition(AbilityEvent.OnResolvingAbility)
    }

    fun getCurrentState() = abilityStateMachine.state

    private fun checkRequirements() {
        // Check resources
        for (resource in abilityInfo.abilityRequirement.resources) {
            if (!resource.hasEnoughResources(source)) {
                abilityStateMachine.transition(AbilityEvent.OnCheckingRequirementsFails)

                return
            }
        }

        abilityStateMachine.transition(AbilityEvent.OnPerformingAbility)
    }

    private fun computeResources() {
        for (resource in abilityInfo.abilityRequirement.resources) {
            resource.computeResources(source)
        }

        abilityStateMachine.transition(AbilityEvent.OnResourcesComputed)
    }

    private fun handleCasting() {
        resumeCasting(false)
    }

    private fun computeEffects() {
        abilityStateMachine.transition(AbilityEvent.OnAbilityResolved)
    }
}

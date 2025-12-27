package fr.rob.game.ability.state

import com.tinder.StateMachine
import fr.rob.game.ability.Ability
import fr.rob.game.ability.AbilityInfo
import fr.rob.game.ability.event.AbilityFailedEvent
import fr.rob.game.ability.launch.LaunchTypeInterface

class AbilityFlow(
    private val ability: Ability,
    private val launchType: LaunchTypeInterface,
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
                    transitionTo(AbilityState.Launching, AbilitySideEffect.HandleLaunching)
                }
            }

            state<AbilityState.Launching> {
                on<AbilityEvent.OnLaunchCompleted> {
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
                    AbilitySideEffect.HandleLaunching -> handleLaunching()
                    AbilitySideEffect.ComputeEffects -> computeEffects()
                    null -> {}
                }
            }
        }

    fun useAbility() {
        abilityStateMachine.transition(AbilityEvent.OnUseAbility)
    }

    fun resumeCasting(isCastingSequenceDone: Boolean) {
        if (ability.info.castingTimeMs != AbilityInfo.INSTANT_CASTING_TIME && !isCastingSequenceDone) {
            return
        }

        abilityStateMachine.transition(AbilityEvent.OnResolvingAbility)
    }

    fun resumeLaunching() {
        if (launchType.isLaunchingCompleted()) {
            abilityStateMachine.transition(AbilityEvent.OnLaunchCompleted)
        }
    }

    fun getCurrentState() = abilityStateMachine.state

    private fun checkRequirements() {
        // Check resources
        for (resource in ability.info.abilityRequirement.resources) {
            if (!resource.hasEnoughResources(ability.source)) {
                val resourceType = resource::class.simpleName ?: "unknown"
                abilityStateMachine.transition(AbilityEvent.OnCheckingRequirementsFails)
                ability.source.pushEvent(AbilityFailedEvent(ability, "Not enough $resourceType"))

                return
            }
        }

        abilityStateMachine.transition(AbilityEvent.OnPerformingAbility)
    }

    private fun computeResources() {
        for (resource in ability.info.abilityRequirement.resources) {
            resource.computeResources(ability.source)
        }

        abilityStateMachine.transition(AbilityEvent.OnResourcesComputed)
    }

    private fun handleCasting() {
        resumeCasting(false)
    }

    private fun handleLaunching() {
        launchType.handleLaunch()
    }

    private fun computeEffects() {
        abilityStateMachine.transition(AbilityEvent.OnAbilityResolved)
    }
}

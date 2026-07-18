using System;
using Game.Ability;
using Game.Entity;
using Game.Input;
using Game.State;
using UnityEngine;
using VContainer.Unity;

namespace Game.Animation
{
    public class PlayerAnimationController : IInitializable, IDisposable
    {
        private static readonly int AnimCastRaiseHash = Animator.StringToHash("CastRaise");
        private static readonly int AnimIsCastingHash = Animator.StringToHash("IsCasting");

        private readonly WorldState _worldState;
        private readonly EntitySpawner _entitySpawner;
        private readonly PlayerInputController _playerInputController;

        private Animator _animator;


        public PlayerAnimationController(
            WorldState worldState, 
            EntitySpawner entitySpawner, 
            PlayerInputController playerInputController
        )
        {
            _worldState = worldState;
            _entitySpawner = entitySpawner;
            _playerInputController = playerInputController;

            _entitySpawner.OnPlayerAnimatorReady += OnPlayerAnimatorReady;
            _playerInputController.OnAbilityCast += OnAbilityCast;
            _worldState.AbilityStateChanged += OnAbilityStateChanged;
        }

        private void OnPlayerAnimatorReady(Animator animator) => _animator = animator;

        private void OnAbilityCast()
        {
            Debug.Log($"[PlayerAnimation] OnAbilityCast called, animator={_animator}");
            _animator?.SetBool(AnimIsCastingHash, true);
            _animator?.SetTrigger(AnimCastRaiseHash);
        }

        private void OnAbilityStateChanged(Ability.Ability ability)
        {
            if (ability.State == AbilityState.Launching)
            {
                _animator?.SetBool(AnimIsCastingHash, false);
            }
            
            if (ability.State == AbilityState.Failed)
            {
                _animator?.ResetTrigger(AnimCastRaiseHash);
            }
        }

        public void Dispose()
        {
            _entitySpawner.OnPlayerAnimatorReady -= OnPlayerAnimatorReady;
            _playerInputController.OnAbilityCast -= OnAbilityCast;
            _worldState.AbilityStateChanged -= OnAbilityStateChanged;

        }

        public void Initialize()
        {
        }
    }
}
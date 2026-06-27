using System.Collections;
using Game.Ability;
using Game.State;
using UnityEngine;
using VContainer;

namespace Game.UI.CastBar
{
    public class CastBarController : MonoBehaviour
    {
        private WorldState _worldState;
        private AbilityDatabase _abilityDatabase;
        private CastBarView _view;
        private Coroutine _castCoroutine;

        public ulong? WatchedEntityGuid { get; private set; }
        public uint? WatchedAbilityId { get; private set; }

        [Inject]
        public void Construct(WorldState worldState, AbilityDatabase abilityDatabase)
        {
            _worldState = worldState;
            _abilityDatabase = abilityDatabase;
            _worldState.AbilityStateChanged += OnAbilityStateChanged;
        }

        private void Awake()
        {
            _view = GetComponent<CastBarView>();
        }

        private void OnDestroy()
        {
            if (_worldState != null)
            {
                _worldState.AbilityStateChanged -= OnAbilityStateChanged;
            }
        }

        private ulong GetWatchedEntityGuid()
        {
            return WatchedEntityGuid ?? _worldState.ControlledEntityId.GetRawValue();
        }

        private void OnAbilityStateChanged(Ability.Ability ability)
        {
            if (ability.SourceGuid != GetWatchedEntityGuid())
            {
                return;
            }

            if (ability.State == AbilityState.Casting)
            {
                var definition = _abilityDatabase.GetDefinition(ability.AbilityInfoId);
                var durationMs = definition?.CastDurationMs ?? 0;

                if (_castCoroutine != null)
                {
                    StopCoroutine(_castCoroutine);
                }

                WatchedAbilityId = ability.AbilityId;
                _castCoroutine = StartCoroutine(RunCastBar(durationMs));
            }
            else if (WatchedAbilityId == ability.AbilityId)
            {
                HideCastBar();
            }
        }

        private IEnumerator RunCastBar(int durationMs)
        {
            _view.Show();
            float elapsed = 0f;

            while (elapsed < durationMs)
            {
                elapsed += Time.deltaTime * 1000f;
                float progress = Mathf.Clamp01(elapsed / durationMs);
                _view.SetProgress(progress);

                yield return null;
            }

            HideCastBar();
        }

        private void HideCastBar()
        {
            if (_castCoroutine != null)
            {
                StopCoroutine(_castCoroutine);
                _castCoroutine = null;
            }

            _view.Hide();
            _view.SetProgress(0f);
        }
    }
}
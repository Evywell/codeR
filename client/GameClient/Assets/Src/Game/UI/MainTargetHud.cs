using Game.Entity;
using Game.State;
using UnityEngine;
using VContainer;

namespace Game.UI
{
    /// <summary>
    /// On-screen HUD displaying the raw GUID value of the controlled entity's main target.
    /// Subscribes to MainTargetChanged on the controlled entity (re-subscribing if the
    /// controlled entity appears or changes).
    /// </summary>
    public class MainTargetHud : MonoBehaviour
    {
        private WorldState _worldState;
        private WorldEntity _subscribedEntity;
        private ObjectGuid _currentTarget;

        [Inject]
        public void Construct(WorldState worldState)
        {
            _worldState = worldState;
        }

        private void Update()
        {
            if (_worldState == null)
                return;

            WorldEntity controlled = _worldState.GetControlledEntity();

            if (controlled == _subscribedEntity)
                return;

            // Controlled entity changed (or appeared) — refresh subscription.
            if (_subscribedEntity != null)
            {
                _subscribedEntity.MainTargetChanged -= OnMainTargetChanged;
            }

            _subscribedEntity = controlled;
            _currentTarget = controlled?.MainTargetGuid;

            if (_subscribedEntity != null)
            {
                _subscribedEntity.MainTargetChanged += OnMainTargetChanged;
            }
        }

        private void OnMainTargetChanged(ObjectGuid newTarget)
        {
            _currentTarget = newTarget;
        }

        private void OnGUI()
        {
            string text = _currentTarget != null
                ? $"Main Target: {_currentTarget.GetRawValue()}"
                : "Main Target: none";

            GUI.Label(new Rect(10f, 10f, 500f, 24f), text);
        }

        private void OnDestroy()
        {
            if (_subscribedEntity != null)
            {
                _subscribedEntity.MainTargetChanged -= OnMainTargetChanged;
                _subscribedEntity = null;
            }
        }
    }
}

using Game.Entity;
using Game.State;
using UnityEngine;
using UnityCamera = UnityEngine.Camera;

namespace Game.Interaction
{
    /// <summary>
    /// Resolves a screen-space point to an entity GUID via raycast and assigns it as the
    /// main target of the controlled entity. Purely client-side — no network message is sent.
    /// </summary>
    public class MainTargetSelector
    {
        private readonly WorldState _worldState;

        public MainTargetSelector(WorldState worldState)
        {
            _worldState = worldState;
        }

        /// <summary>
        /// Raycasts from the given screen position through the given camera. If an EntityView
        /// is hit, its GUID becomes the controlled entity's main target. If nothing is hit,
        /// the main target is cleared.
        /// </summary>
        public void SelectFromScreenPoint(Vector2 screenPos, UnityCamera cam)
        {
            if (cam == null)
                return;

            WorldEntity controlled = _worldState.GetControlledEntity();

            if (controlled == null)
                return;

            Ray ray = cam.ScreenPointToRay(new Vector3(screenPos.x, screenPos.y, 0f));

            if (Physics.Raycast(ray, out RaycastHit hit, Mathf.Infinity, ~0, QueryTriggerInteraction.Collide))
            {
                EntityView entityView = hit.transform.GetComponentInParent<EntityView>();

                if (entityView != null && entityView.Entity != null)
                {
                    controlled.MainTargetGuid = entityView.Entity.Guid;
                    return;
                }
            }

            controlled.MainTargetGuid = null;
        }
    }
}

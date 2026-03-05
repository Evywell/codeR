using UnityEngine;
using UnityEngine.AI;

namespace Game.Entity
{
    /// <summary>
    /// MonoBehaviour attached to non-player entity GameObjects.
    /// Uses NavMeshAgent for smooth ground-constrained movement that follows
    /// server-issued waypoints.
    ///
    /// The server's PhysicServer computes a full NavMesh path, splits it into
    /// waypoints (path corners), and sends them one at a time via
    /// SMSG_OBJECT_MOVING_TO_DESTINATION. Each message is the next waypoint
    /// along the path. This component calls SetDestination() for each waypoint,
    /// producing smooth movement that faithfully follows the server's route.
    ///
    /// Heartbeat packets (SMSG_MOVEMENT_HEARTBEAT) provide authoritative position
    /// corrections — used only for large drift correction (warp).
    /// </summary>
    public class EntityMotion : MonoBehaviour
    {
        private WorldEntity _entity;
        private NavMeshAgent _agent;

        /// <summary>
        /// Tracks the last waypoint we sent to NavMeshAgent.SetDestination()
        /// so we only call it when a new waypoint arrives.
        /// </summary>
        private Vector3? _appliedWaypoint;

        /// <summary>
        /// Large snap threshold — if the agent is this far from the server
        /// heartbeat position, warp unconditionally (e.g., teleport, desync).
        /// </summary>
        private const float SnapDistanceThreshold = 5f;

        public void Initialize(WorldEntity entity, NavMeshAgent agent)
        {
            _entity = entity;
            _agent = agent;
        }

        private void Update()
        {
            if (_entity == null || _agent == null)
                return;

            SyncSpeed();
            HandleWaypoint();
            HandleDriftCorrection();
            HandleStop();
        }

        /// <summary>
        /// Keep NavMeshAgent speed in sync with the server-reported entity speed.
        /// </summary>
        private void SyncSpeed()
        {
            if (_entity.Speed > 0f)
            {
                _agent.speed = _entity.Speed;
            }
        }

        /// <summary>
        /// When a new waypoint arrives from SMSG_OBJECT_MOVING_TO_DESTINATION,
        /// navigate the agent toward it. Each message is the next waypoint along
        /// the server's pre-computed path (already in Unity coordinate space).
        /// </summary>
        private void HandleWaypoint()
        {
            Vector3? requested = _entity.LastRequestedDestination;

            if (!requested.HasValue)
                return;

            // Only call SetDestination when the waypoint actually changed
            if (_appliedWaypoint.HasValue && _appliedWaypoint.Value == requested.Value)
                return;

            Vector3 waypoint = requested.Value;
            waypoint.y = 0f; // Force ground level

            _agent.SetDestination(waypoint);
            _appliedWaypoint = requested.Value;
        }

        /// <summary>
        /// Heartbeat packets provide authoritative position along the path.
        /// Only warp if we've drifted significantly — otherwise let the
        /// NavMeshAgent reach the waypoint naturally.
        /// </summary>
        private void HandleDriftCorrection()
        {
            if (!_entity.IsMoving)
                return;

            Vector3 serverPos = _entity.Position;
            serverPos.y = 0f;

            float drift = Vector3.Distance(transform.position, serverPos);

            if (drift > SnapDistanceThreshold)
            {
                _agent.Warp(serverPos);
            }
        }

        /// <summary>
        /// When the server sends PHASE_END (entity.IsMoving becomes false),
        /// stop the agent and snap to the final authoritative position.
        /// </summary>
        private void HandleStop()
        {
            if (_entity.IsMoving)
                return;

            if (_agent.hasPath)
            {
                _agent.ResetPath();
            }

            // Snap to final server position
            Vector3 finalPos = _entity.Position;
            finalPos.y = 0f;

            float drift = Vector3.Distance(transform.position, finalPos);

            if (drift > 0.05f)
            {
                _agent.Warp(finalPos);
            }

            _appliedWaypoint = null;
            _entity.LastRequestedDestination = null;
        }
    }
}

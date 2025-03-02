using App.Network;
using Fr.Raven.Proto.Message.Physicbridge;
using UnityEngine;
using UnityEngine.AI;
using Google.Protobuf;

namespace App {
    public class ObjectController : MonoBehaviour {
        public ulong entityGuid;
        public GameObject waypointPrefab;

        private NavMeshAgent _navMeshAgent;
        private bool _hasOngoingMovement = false;
        private GameObject[] _waypoints;

        public void MoveToPosition(Vector3 position)
        {
            InitNavMeshAgent();

            _hasOngoingMovement = true;

            NavMeshPath path = new NavMeshPath();

            _navMeshAgent.CalculatePath(position, path);

            var corners = path.corners;

            if (corners.Length < 1) {
                return;
            }

            InstantiateWaypoints(corners);

            _navMeshAgent.SetPath(path);

            SendMovement(corners[1]);
        }

        public void OnWaypointEntered(int index)
        {
            Destroy(_waypoints[index]);

            var isLastWaypoint = _waypoints.Length - 1 == index;

            if (isLastWaypoint) {
                Debug.Log($"Last waypoint reached by {entityGuid}");

                _navMeshAgent.ResetPath();
                return;
            }

            Debug.Log($"Current index {index} / Next index {index + 1} for {entityGuid}");

            var nextWaypoint = _waypoints[index + 1];

            SendMovement(nextWaypoint.transform.position);
        }

        private void SendMovement(Vector3 position)
        {
            var objectMoveTo = new ObjectMoveTo {
                Guid = entityGuid,
                Destination = new Vec3 {
                    X = position.x,
                    Y = position.y,
                    Z = position.z,
                }
            };

            PhysicServer.Instance.SendMessageToEveryone(new Packet {
                Opcode = 0x03,
                Body = objectMoveTo.ToByteString()
            });
        }

        private void InstantiateWaypoints(Vector3[] corners)
        {
            GameObject[] waypoints = new GameObject[corners.Length - 1];

            for (int i = 0; i < corners.Length - 1; i++) {
                GameObject waypoint = Instantiate(waypointPrefab, corners[i + 1], Quaternion.identity);
                waypoint.GetComponent<WaypointTrigger>().RegisterWaypoint(this, i);

                waypoints[i] = waypoint;
            }

            _waypoints = waypoints;
        }

        private void Start()
        {
            InitNavMeshAgent();
        }

        private void Update()
        {
            var rayStartPosition = transform.position + new Vector3(0, 2, 0);
            var directionVector = new Vector3(transform.forward.x, 0, transform.forward.z).normalized;

            Debug.DrawRay(
                rayStartPosition, 
                directionVector, 
                Color.red, 
                0, 
                false
            );

            if (!_hasOngoingMovement) {
                return;
            }

            if (_navMeshAgent.remainingDistance > 0) {
                return;
            }

            PhysicServer.Instance.SendMessageToEveryone(new Packet {
                Opcode = 0x02,
                Body = new ObjectReachDestination {
                    Guid = entityGuid,
                }.ToByteString()
            });

            _hasOngoingMovement = false;
        }

        private void InitNavMeshAgent()
        {
            if (_navMeshAgent != null) {
                return;
            }

            _navMeshAgent = GetComponent<NavMeshAgent>();
        }
    }
}
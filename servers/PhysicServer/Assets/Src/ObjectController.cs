using App.Network;
using Fr.Raven.Proto.Message.Physicbridge;
using UnityEngine;
using UnityEngine.AI;
using Google.Protobuf;

namespace App {
    public class ObjectController : MonoBehaviour {
        public ulong entityGuid;

        private NavMeshAgent _navMeshAgent;
        private bool _hasOngoingMovement = false;
        private Vector3 _lastSteeringTarget;

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

            _navMeshAgent.SetPath(path);
        }

        private void SendMovement(Vector3 position)
        {
            var objectMoveTo = new ObjectMoveTo {
                Guid = entityGuid,
                Destination = new Vec3 {
                    X = position.x,
                    Y = position.z,
                    Z = position.y,
                }
            };

            PhysicServer.Instance.SendMessageToEveryone(new Packet {
                Opcode = 0x03,
                Body = objectMoveTo.ToByteString()
            });
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

            if (_lastSteeringTarget != _navMeshAgent.steeringTarget) {
                _lastSteeringTarget = _navMeshAgent.steeringTarget;

                SendMovement(_lastSteeringTarget);
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
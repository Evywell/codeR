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

        public void MoveToPosition(Vector3 position)
        {
            InitNavMeshAgent();

            _hasOngoingMovement = true;
            _navMeshAgent.SetDestination(position);
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
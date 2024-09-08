using Google.Protobuf;
using UnityEngine;
using Fr.Raven.Proto.Message.Physicbridge;
using App.Normalizers;

namespace App.Network {
    public class TransformSyncOverNetwork : MonoBehaviour {
        public Transform Transform
        { get; private set; }

        public ulong entityGuid;

        private Vector3 _currentPosition;
        private bool _hasPositionXChanged = false;
        private bool _hasPositionYChanged = false;
        private bool _hasPositionZChanged = false;
        private float _nextPositionUpdateTime = 0;

        private void Start() {
            Transform = GetComponent<Transform>();
            _currentPosition = new Vector3(transform.position.x, transform.position.y, transform.position.z);
        }

        private void Update() {
            DetectTransformIsDirty();

            if (IsCurrentPositionDirty() && Time.fixedTime >= _nextPositionUpdateTime) {
                SynchronizePosition();

                _nextPositionUpdateTime = Time.fixedTime + 0.033f; // Every 33ms
            }
        }

        private void SynchronizePosition() {
            _currentPosition.x = Transform.position.x;
            _currentPosition.y = Transform.position.y;
            _currentPosition.z = Transform.position.z;

            _hasPositionXChanged = false;
            _hasPositionYChanged = false;
            _hasPositionZChanged = false;

            float orientationRad = transform.rotation.eulerAngles.y * Mathf.Deg2Rad;

            float orientation = NormalizeOrientation(PositionNormalizer.TransformUnityOrientationToServerOrientation(orientationRad));

            PhysicServer.Instance.SendMessageToEveryone(new Packet
            {
                Opcode = 0x01,
                Body = new ObjectMoved {
                    Guid = entityGuid,
                    Position = new Position
                    {
                        PosX = _currentPosition.x,
                        PosY = _currentPosition.z,
                        PosZ = _currentPosition.y,
                        Orientation = orientation
                    }
                }.ToByteString()
            });
        }

        private void DetectTransformIsDirty()
        {
            if (_currentPosition.x != Transform.position.x) {
                _hasPositionXChanged = true;
            }

            if (_currentPosition.y != Transform.position.y) {
                _hasPositionYChanged = true;
            }

            if (_currentPosition.z != Transform.position.z) {
                _hasPositionZChanged = true;
            }
        }

        private bool IsCurrentPositionDirty() {
            return _hasPositionXChanged || _hasPositionYChanged || _hasPositionZChanged;
        }

        private float NormalizeOrientation(float orientation) {
            return orientation % (2 * Mathf.PI);
        }
    }
}
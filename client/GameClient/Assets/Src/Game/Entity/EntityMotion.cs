using Core.Movement;
using UnityEngine;

namespace Game.Entity
{
    /// <summary>
    /// MonoBehaviour attached to non-player entity GameObjects.
    /// Smoothly interpolates the transform toward the WorldEntity's authoritative position
    /// and orientation each frame.
    /// </summary>
    public class EntityMotion : MonoBehaviour
    {
        private WorldEntity _entity;
        private float _positionLerpSpeed = 8f;
        private float _rotationLerpSpeed = 10f;
        private const float SnapDistanceThreshold = 10f;

        public void Initialize(WorldEntity entity)
        {
            _entity = entity;
        }

        private void Update()
        {
            if (_entity == null)
                return;

            // Interpolate position toward authoritative server position.
            // Force Y to 0 (ground level) — the visual capsule child at localY=1
            // handles the rendering offset. The server Y value is its internal ground
            // height (~0.7) which doesn't match Unity's ground plane at Y=0.
            Vector3 targetPos = _entity.Position;
            targetPos.y = 0f;

            // Snap instantly if the gap is too large (e.g., teleport), otherwise lerp smoothly
            float distance = Vector3.Distance(transform.position, targetPos);

            if (distance > SnapDistanceThreshold)
            {
                transform.position = targetPos;
            }
            else
            {
                transform.position = Vector3.Lerp(
                    transform.position,
                    targetPos,
                    Time.deltaTime * _positionLerpSpeed
                );
            }

            // Convert server orientation (radians) to Unity euler Y (degrees)
            float unityOrientationRad = PositionNormalizer.TransformServerOrientationToUnityOrientation(_entity.Orientation);
            float targetYDegrees = unityOrientationRad * Mathf.Rad2Deg;

            Quaternion targetRotation = Quaternion.Euler(0f, targetYDegrees, 0f);
            transform.rotation = Quaternion.Slerp(
                transform.rotation,
                targetRotation,
                Time.deltaTime * _rotationLerpSpeed
            );
        }
    }
}

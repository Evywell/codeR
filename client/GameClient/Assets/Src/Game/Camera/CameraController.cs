using Game.Entity;
using Game.Input;
using Game.State;
using UnityEngine;

namespace Game.Camera
{
    /// <summary>
    /// Third-person orbit camera that follows the player entity.
    /// Right-click drag (or cursor-locked mode) orbits around the player.
    /// Scroll wheel zooms in/out. Pitch is clamped to avoid flipping.
    ///
    /// Uses GameInput (.inputactions asset) for Look and Zoom input.
    /// Attach this to the Main Camera programmatically via GameWorldManager.
    /// </summary>
    public class CameraController : MonoBehaviour
    {
        private EntitySpawner _entitySpawner;
        private WorldState _worldState;
        private GameInput _input;

        private float _yaw;
        private float _pitch = 20f;
        private float _distance = 10f;
        private Vector3 _targetOffset = new Vector3(0f, 1.5f, 0f);

        private const float MouseSensitivity = 0.15f;
        private const float ScrollSensitivity = 3f;
        private const float MinDistance = 3f;
        private const float MaxDistance = 25f;
        private const float MinPitch = -30f;
        private const float MaxPitch = 70f;
        private const float FollowSmoothSpeed = 10f;

        public void Initialize(EntitySpawner entitySpawner, WorldState worldState, GameInput input)
        {
            _entitySpawner = entitySpawner;
            _worldState = worldState;
            _input = input;

            // Lock cursor at start for camera control
            Cursor.lockState = CursorLockMode.Locked;
            Cursor.visible = false;
        }

        private void LateUpdate()
        {
            if (_entitySpawner == null || _input == null)
                return;

            GameObject playerGO = _entitySpawner.PlayerGameObject;

            if (playerGO == null)
                return;

            // Read look delta from new Input System
            Vector2 lookDelta = _input.Gameplay.Look.ReadValue<Vector2>();

            // Orbit with right mouse button held, or always when cursor is locked
            bool shouldOrbit = UnityEngine.InputSystem.Mouse.current != null
                && UnityEngine.InputSystem.Mouse.current.rightButton.isPressed
                || Cursor.lockState == CursorLockMode.Locked;

            if (shouldOrbit)
            {
                _yaw += lookDelta.x * MouseSensitivity;
                _pitch -= lookDelta.y * MouseSensitivity;
                _pitch = Mathf.Clamp(_pitch, MinPitch, MaxPitch);
            }

            // Scroll wheel zoom via new Input System
            Vector2 scrollDelta = _input.Gameplay.Zoom.ReadValue<Vector2>();

            if (Mathf.Abs(scrollDelta.y) > 0.001f)
            {
                _distance -= scrollDelta.y * ScrollSensitivity * Time.deltaTime;
                _distance = Mathf.Clamp(_distance, MinDistance, MaxDistance);
            }

            // Compute camera position orbiting around the player
            Vector3 pivotPoint = playerGO.transform.position + _targetOffset;
            Quaternion rotation = Quaternion.Euler(_pitch, _yaw, 0f);
            Vector3 offset = rotation * new Vector3(0f, 0f, -_distance);

            Vector3 desiredPosition = pivotPoint + offset;

            transform.position = Vector3.Lerp(transform.position, desiredPosition, Time.deltaTime * FollowSmoothSpeed);
            transform.LookAt(pivotPoint);
        }
    }
}

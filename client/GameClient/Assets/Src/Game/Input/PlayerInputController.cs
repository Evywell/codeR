using Core.Movement;
using Game.Entity;
using Game.Interaction;
using Game.State;
using UnityEngine;
using UnityEngine.InputSystem;
using VContainer.Unity;
using UnityCamera = UnityEngine.Camera;

namespace Game.Input
{
    /// <summary>
    /// Reads input from GameInput (new Input System, .inputactions asset) each frame.
    /// Computes a camera-relative movement direction, moves the player via CharacterController,
    /// and replicates position to the server at 5Hz.
    ///
    /// Also handles keybinds: Q = spell cast, E = click-to-target attack, Escape = unlock cursor,
    /// Left click = re-lock cursor.
    /// </summary>
    public class PlayerInputController : ITickable
    {
        private readonly WorldState _worldState;
        private readonly EntitySpawner _entitySpawner;
        private readonly MovementSender _movementSender;
        private readonly SpellCaster _spellCaster;
        private readonly CombatEngager _combatEngager;
        private readonly GameInput _input;

        private const float MoveSpeed = 5f;
        private const float RotationSmoothSpeed = 12f;
        private const float Gravity = -15f;
        private const float ServerUpdateInterval = 0.2f; // 5Hz

        private float _verticalVelocity;
        private float _timeSinceLastServerUpdate;
        private Vector3 _lastSentPosition;
        private bool _hasSentInitialPosition;

        public PlayerInputController(
            WorldState worldState,
            EntitySpawner entitySpawner,
            MovementSender movementSender,
            SpellCaster spellCaster,
            CombatEngager combatEngager,
            GameInput input)
        {
            _worldState = worldState;
            _entitySpawner = entitySpawner;
            _movementSender = movementSender;
            _spellCaster = spellCaster;
            _combatEngager = combatEngager;
            _input = input;
        }

        public void Tick()
        {
            CharacterController cc = _entitySpawner.PlayerCharacterController;

            if (cc == null)
                return;

            HandleMovement(cc);
            HandleKeybinds();
            HandleCursorLock();
        }

        private void HandleMovement(CharacterController cc)
        {
            Transform playerTransform = cc.transform;

            // Read WASD / stick input as Vector2
            Vector2 moveInput = _input.Gameplay.Move.ReadValue<Vector2>();
            Vector3 inputDir = new Vector3(moveInput.x, 0f, moveInput.y).normalized;

            // Compute camera-relative direction
            Vector3 moveDir = Vector3.zero;

            if (inputDir.sqrMagnitude > 0.01f)
            {
                UnityCamera mainCam = UnityCamera.main;

                if (mainCam != null)
                {
                    float cameraYaw = mainCam.transform.eulerAngles.y;
                    moveDir = Quaternion.Euler(0f, cameraYaw, 0f) * inputDir;
                    moveDir.y = 0f;
                    moveDir.Normalize();

                    // Rotate player to face movement direction
                    float targetAngle = Mathf.Atan2(moveDir.x, moveDir.z) * Mathf.Rad2Deg;
                    float smoothedAngle = Mathf.LerpAngle(
                        playerTransform.eulerAngles.y,
                        targetAngle,
                        Time.deltaTime * RotationSmoothSpeed
                    );
                    playerTransform.rotation = Quaternion.Euler(0f, smoothedAngle, 0f);
                }
            }

            // Apply gravity
            if (cc.isGrounded && _verticalVelocity < 0f)
            {
                _verticalVelocity = -2f; // Small downward force to keep grounded
            }

            _verticalVelocity += Gravity * Time.deltaTime;

            Vector3 velocity = moveDir * MoveSpeed + Vector3.up * _verticalVelocity;
            cc.Move(velocity * Time.deltaTime);

            // Update the WorldEntity data model so the server stays in sync
            WorldEntity playerEntity = _worldState.GetControlledEntity();

            if (playerEntity != null)
            {
                playerEntity.Position = playerTransform.position;
                playerEntity.Orientation = playerTransform.eulerAngles.y * Mathf.Deg2Rad;
                playerEntity.IsMoving = inputDir.sqrMagnitude > 0.01f;
                playerEntity.Direction = moveDir;
            }

            // Send movement to server at 5Hz
            SendMovementToServer(playerTransform, moveDir);
        }

        private void SendMovementToServer(Transform playerTransform, Vector3 direction)
        {
            _timeSinceLastServerUpdate += Time.deltaTime;

            if (_timeSinceLastServerUpdate < ServerUpdateInterval)
                return;

            Vector3 currentPos = playerTransform.position;

            // Skip if position hasn't changed and we've already sent the initial position
            if (_hasSentInitialPosition && currentPos == _lastSentPosition)
                return;

            _timeSinceLastServerUpdate = 0f;
            _lastSentPosition = currentPos;
            _hasSentInitialPosition = true;

            // Convert Unity orientation to server orientation
            float unityOrientationRad = playerTransform.eulerAngles.y * Mathf.Deg2Rad;
            float serverOrientation = PositionNormalizer.TransformUnityOrientationToServerOrientation(unityOrientationRad);
            serverOrientation = serverOrientation % (2f * Mathf.PI);

            // Unity(x,y,z) -> Server(x,z,y)
            _movementSender.SendMovement(
                currentPos.x,
                currentPos.z,
                currentPos.y,
                serverOrientation,
                new Vector3(direction.x, direction.z, 0f)
            );
        }

        private void HandleKeybinds()
        {
            // Q = cast spell (hardcoded spell ID 1, matching RobAlpha)
            if (_input.Gameplay.CastSpell.WasPerformedThisFrame())
            {
                _spellCaster.CastSpell(1);
                Debug.Log("[PlayerInput] Cast spell 1");
            }

            // E = click-to-target attack (raycast from cursor)
            if (_input.Gameplay.EngageCombat.WasPerformedThisFrame())
            {
                TryEngageCombat();
            }
        }

        private void TryEngageCombat()
        {
            UnityCamera mainCam = UnityCamera.main;

            if (mainCam == null)
                return;

            Vector2 mousePos = Mouse.current != null ? Mouse.current.position.ReadValue() : Vector2.zero;
            Ray ray = mainCam.ScreenPointToRay(new Vector3(mousePos.x, mousePos.y, 0f));

            if (Physics.Raycast(ray, out RaycastHit hit))
            {
                EntityView entityView = hit.transform.GetComponentInParent<EntityView>();

                if (entityView != null && entityView.Entity != null)
                {
                    _combatEngager.EngageCombat(entityView.Entity.Guid);
                    Debug.Log($"[PlayerInput] Engaging combat with {entityView.Entity.Guid.GetRawValue():X16}");
                }
            }
        }

        private void HandleCursorLock()
        {
            // Escape unlocks the cursor
            if (_input.Gameplay.ToggleCursor.WasPerformedThisFrame())
            {
                Cursor.lockState = CursorLockMode.None;
                Cursor.visible = true;
            }

            // Left click re-locks the cursor when it's unlocked
            if (Cursor.lockState != CursorLockMode.Locked
                && Mouse.current != null
                && Mouse.current.leftButton.wasPressedThisFrame)
            {
                Cursor.lockState = CursorLockMode.Locked;
                Cursor.visible = false;
            }
        }
    }
}

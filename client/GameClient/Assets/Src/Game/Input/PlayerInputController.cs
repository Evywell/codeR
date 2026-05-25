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
        private const float TurnSpeed = 140f; // deg/s for A/D yaw turning
        private const float Gravity = -15f;
        private const float ServerUpdateInterval = 0.2f; // 5Hz
        private const float AnimatorSpeedSmoothing = 10f;

        // StarterAssets animator parameter hashes (controller: StarterAssetsThirdPerson)
        private static readonly int AnimSpeedHash = Animator.StringToHash("Speed");
        private static readonly int AnimGroundedHash = Animator.StringToHash("Grounded");
        private static readonly int AnimJumpHash = Animator.StringToHash("Jump");
        private static readonly int AnimFreeFallHash = Animator.StringToHash("FreeFall");
        private static readonly int AnimMotionSpeedHash = Animator.StringToHash("MotionSpeed");

        private float _verticalVelocity;
        private float _timeSinceLastServerUpdate;
        private Vector3 _lastSentPosition;
        private bool _hasSentInitialPosition;
        private float _animatorSpeed;

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

            // WoW-style controls (the StarterAssets pack only ships forward/backward
            // animations — no strafe clips — so A/D rotates the character instead of
            // strafing, keeping animations coherent with the actual motion).
            //   moveInput.y (W/S) -> forward/backward translation
            //   moveInput.x (A/D) -> yaw rotation
            Vector2 moveInput = _input.Gameplay.Move.ReadValue<Vector2>();
            float forwardInput = moveInput.y;     // -1..1
            float turnInput = moveInput.x;        // -1..1
            bool isMoving = Mathf.Abs(forwardInput) > 0.01f;
            bool isTurning = Mathf.Abs(turnInput) > 0.01f;

            // Apply yaw rotation from A/D
            if (isTurning)
            {
                playerTransform.Rotate(0f, turnInput * TurnSpeed * Time.deltaTime, 0f);
            }

            // Translate along the player's local forward axis
            Vector3 moveDir = Vector3.zero;

            if (isMoving)
            {
                moveDir = playerTransform.forward * Mathf.Sign(forwardInput);
            }

            // Apply gravity
            if (cc.isGrounded && _verticalVelocity < 0f)
            {
                _verticalVelocity = -2f; // Small downward force to keep grounded
            }

            _verticalVelocity += Gravity * Time.deltaTime;

            // Backward speed is reduced, like most MMOs
            float forwardSpeed = forwardInput >= 0f ? MoveSpeed : MoveSpeed * 0.6f;
            Vector3 velocity = moveDir * forwardSpeed + Vector3.up * _verticalVelocity;
            cc.Move(velocity * Time.deltaTime);

            // Update the WorldEntity data model so the server stays in sync
            WorldEntity playerEntity = _worldState.GetControlledEntity();

            if (playerEntity != null)
            {
                playerEntity.Position = playerTransform.position;
                playerEntity.Orientation = playerTransform.eulerAngles.y * Mathf.Deg2Rad;
                playerEntity.IsMoving = isMoving;
                playerEntity.Direction = moveDir;
            }

            // Send movement to server at 5Hz (also send when only turning, so server
            // sees the new orientation even without translation).
            SendMovementToServer(playerTransform, moveDir, isMoving || isTurning);

            // Drive humanoid animator. Signed forwardInput drives the bipolar 1D blend
            // tree (negative -> Run_S backpedal, positive -> Walk_N / Run_N).
            UpdateAnimator(cc, forwardInput);
        }

        private void UpdateAnimator(CharacterController cc, float forwardInput)
        {
            Animator animator = _entitySpawner.PlayerAnimator;

            if (animator == null)
                return;

            // Map input to Speed parameter range expected by the blend tree:
            //   forwardInput  1  -> +5 (Run_N region)
            //   forwardInput  0  ->  0 (Idle)
            //   forwardInput -1  -> -5 (Run_S region, backpedal)
            float targetSpeed = forwardInput * MoveSpeed;
            _animatorSpeed = Mathf.Lerp(_animatorSpeed, targetSpeed, Time.deltaTime * AnimatorSpeedSmoothing);

            if (Mathf.Abs(_animatorSpeed) < 0.01f)
            {
                _animatorSpeed = 0f;
            }

            animator.SetFloat(AnimSpeedHash, _animatorSpeed);
            animator.SetFloat(AnimMotionSpeedHash, Mathf.Abs(forwardInput) > 0.01f ? 1f : 0f);
            animator.SetBool(AnimGroundedHash, cc.isGrounded);
            // Jump/FreeFall not used yet — keep them off so we stay in the locomotion blend tree.
            animator.SetBool(AnimJumpHash, false);
            animator.SetBool(AnimFreeFallHash, !cc.isGrounded && _verticalVelocity < -2f);
        }

        private void SendMovementToServer(Transform playerTransform, Vector3 direction, bool isActive)
        {
            _timeSinceLastServerUpdate += Time.deltaTime;

            if (_timeSinceLastServerUpdate < ServerUpdateInterval)
                return;

            Vector3 currentPos = playerTransform.position;

            // Skip if neither moving nor turning, and we've already sent the initial position.
            // (Without this, idle would still keep the gate open after the first send.)
            if (!isActive && _hasSentInitialPosition && currentPos == _lastSentPosition)
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

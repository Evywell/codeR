using System;
using System.Collections.Generic;
using Core.Movement;
using Game.State;
using UnityEngine;
using UnityEngine.AI;
using Object = UnityEngine.Object;

namespace Game.Entity
{
    /// <summary>
    /// Listens to WorldState entity updates and instantiates/manages GameObjects in the scene.
    /// Creates a basic capsule for each entity on first spawn. When the controlled player entity
    /// is identified, upgrades it: replaces the capsule visual with the configured humanoid
    /// prefab (typically StarterAssets PlayerArmature) and adds a CharacterController on the root
    /// for input-driven movement.
    /// </summary>
    public class EntitySpawner
    {
        private readonly WorldState _worldState;
        private readonly GameObject _playerVisualPrefab;
        private readonly Dictionary<ulong, EntityView> _entityViews = new Dictionary<ulong, EntityView>();

        private bool _isPlayerSpawned;

        /// <summary>
        /// The controlled player's GameObject, set after the player entity is upgraded.
        /// </summary>
        public GameObject PlayerGameObject { get; private set; }

        /// <summary>
        /// The CharacterController on the player entity, used by PlayerInputController.
        /// </summary>
        public CharacterController PlayerCharacterController { get; private set; }

        /// <summary>
        /// The Animator on the instantiated player visual (humanoid armature).
        /// Null if the configured prefab has no Animator (e.g. in pure-capsule fallback).
        /// </summary>
        public Animator PlayerAnimator { get; private set; }
        public event Action<Animator> OnPlayerAnimatorReady;

        public EntitySpawner(WorldState worldState, GameObject playerVisualPrefab)
        {
            _worldState = worldState;
            _playerVisualPrefab = playerVisualPrefab;
            _worldState.EntityUpdated += OnEntityUpdated;
        }

        public void Dispose()
        {
            _worldState.EntityUpdated -= OnEntityUpdated;
        }

        /// <summary>
        /// Called each frame to check if the player entity needs upgrading from a basic NPC
        /// capsule to a player-controlled entity (with humanoid visual + CharacterController,
        /// no EntityMotion).
        /// </summary>
        public void Tick()
        {
            TryUpgradePlayerEntity();
        }

        public EntityView GetView(ulong rawGuid)
        {
            _entityViews.TryGetValue(rawGuid, out EntityView view);
            return view;
        }

        private void OnEntityUpdated(WorldEntityUpdate update)
        {
            if (update.Type != WorldUpdateType.Spawn)
                return;

            ulong rawGuid = update.Entity.Guid.GetRawValue();

            if (_entityViews.ContainsKey(rawGuid))
                return;

            EntityView view = CreateBasicEntityView(update.Entity);
            _entityViews[rawGuid] = view;
        }

        private EntityView CreateBasicEntityView(WorldEntity entity)
        {
            // Create an empty root GO whose transform sits at ground level.
            // The visual capsule is a child offset upward so the bottom of the
            // mesh aligns with the root position (Y=0 on the ground plane).
            GameObject go = new GameObject($"Entity_{entity.Guid.GetRawValue():X16}");

            // Create capsule mesh as a child, offset so its bottom sits at the root origin
            GameObject capsuleChild = GameObject.CreatePrimitive(PrimitiveType.Capsule);
            capsuleChild.name = "Visual";
            capsuleChild.transform.SetParent(go.transform);
            capsuleChild.transform.localPosition = new Vector3(0f, 1f, 0f);
            capsuleChild.transform.localRotation = Quaternion.identity;

            // Set initial transform from entity data.
            // Force Y to 0 (ground level) — the child capsule visual at localY=1
            // handles the rendering offset. Gravity handles this for the player entity.
            Vector3 spawnPos = entity.Position;
            spawnPos.y = 0f;
            go.transform.position = spawnPos;

            float unityOrientationRad = PositionNormalizer.TransformServerOrientationToUnityOrientation(entity.Orientation);
            go.transform.rotation = Quaternion.Euler(0f, unityOrientationRad * Mathf.Rad2Deg, 0f);

            // Attach EntityView to link data model to GameObject
            EntityView view = go.AddComponent<EntityView>();
            view.Initialize(entity);

            // Add a CapsuleCollider on the ROOT (not the child visual) for raycast picking
            // (e.g. main target selection). The child capsule collider is removed below to
            // avoid conflicting with NavMeshAgent, so we put a picking collider here instead.
            // Root position sits at Y=0 (ground); the visual is offset to localY=1, so the
            // collider center matches that offset.
            CapsuleCollider pickingCollider = go.AddComponent<CapsuleCollider>();
            pickingCollider.center = new Vector3(0f, 1f, 0f);
            pickingCollider.radius = 0.5f;
            pickingCollider.height = 2f;
            pickingCollider.direction = 1; // Y-axis
            pickingCollider.isTrigger = true; // not a physics blocker; only for raycasts

            // For non-player entities: add NavMeshAgent for server-driven pathfinding.
            // The player entity will be upgraded later (CharacterController replaces this).
            // Remove the CapsuleCollider on the child visual — it conflicts with NavMeshAgent.
            CapsuleCollider childCollider = capsuleChild.GetComponent<CapsuleCollider>();

            if (childCollider != null)
            {
                Object.Destroy(childCollider);
            }

            NavMeshAgent agent = go.AddComponent<NavMeshAgent>();
            agent.height = 2f;
            agent.radius = 0.5f;
            agent.baseOffset = 0f;
            agent.speed = entity.Speed;
            agent.updateRotation = true;
            agent.updatePosition = true;

            // Attach EntityMotion for NavMeshAgent-driven movement
            EntityMotion motion = go.AddComponent<EntityMotion>();
            motion.Initialize(entity, agent);

            // Color by entity type
            Color color;

            if (entity is PlayerEntity)
            {
                color = Color.cyan;
            }
            else
            {
                GuidType guidType = entity.Guid.GetGuidType();

                switch (guidType)
                {
                    case GuidType.ScriptableObject:
                        color = Color.red;
                        break;
                    case GuidType.GameObject:
                        color = Color.green;
                        break;
                    default:
                        color = Color.gray;
                        break;
                }
            }

            SetColor(capsuleChild, color);

            Debug.Log($"[EntitySpawner] Spawned {go.name} at {entity.Position}");
            return view;
        }

        private void TryUpgradePlayerEntity()
        {
            if (_isPlayerSpawned)
                return;

            ObjectGuid controlledId = _worldState.ControlledEntityId;

            if (controlledId == null)
                return;

            ulong rawGuid = controlledId.GetRawValue();

            if (!_entityViews.TryGetValue(rawGuid, out EntityView view))
                return;

            // Remove EntityMotion — player is input-driven, not server-interpolated
            EntityMotion motion = view.GetComponent<EntityMotion>();

            if (motion != null)
            {
                Object.Destroy(motion);
            }

            // Remove NavMeshAgent — player uses CharacterController, not NavMesh pathfinding
            NavMeshAgent agent = view.GetComponent<NavMeshAgent>();

            if (agent != null)
            {
                Object.Destroy(agent);
            }

            // Remove the capsule visual (and its collider) created by CreateBasicEntityView.
            // It will be replaced by the humanoid armature instance below.
            Transform visualChild = view.transform.Find("Visual");

            if (visualChild != null)
            {
                Object.Destroy(visualChild.gameObject);
            }

            // Instantiate the humanoid prefab as a child of the entity root.
            // The prefab's local origin is at the feet (Y=0), matching our root convention.
            if (_playerVisualPrefab != null)
            {
                GameObject visualInstance = Object.Instantiate(_playerVisualPrefab, view.transform);
                visualInstance.name = "Visual";
                visualInstance.transform.localPosition = Vector3.zero;
                visualInstance.transform.localRotation = Quaternion.identity;
                visualInstance.transform.localScale = Vector3.one;

                StripStarterAssetsScripts(visualInstance);
                StripCharacterControllerOnVisual(visualInstance);

                PlayerAnimator = visualInstance.GetComponentInChildren<Animator>();

                // The animator's GameObject is where Unity dispatches AnimationEvents.
                // Attach a stub receiver to silence "OnFootstep/OnLand has no receiver" warnings.
                if (PlayerAnimator != null)
                {
                    PlayerAnimator.gameObject.AddComponent<AnimationEventReceiver>();
                    OnPlayerAnimatorReady?.Invoke(PlayerAnimator);
                }
            }
            else
            {
                Debug.LogWarning("[EntitySpawner] No player visual prefab configured — player will be invisible. " +
                                 "Assign a humanoid prefab on GameLifetimeScope.");
            }

            // Add CharacterController on the root GO.
            // Values match the StarterAssets PlayerArmature so the physics collider
            // wraps the humanoid mesh correctly (feet at Y=0, head ~1.8).
            CharacterController cc = view.gameObject.AddComponent<CharacterController>();
            cc.height = 1.8f;
            cc.radius = 0.28f;
            cc.center = new Vector3(0f, 0.93f, 0f);
            cc.slopeLimit = 45f;
            cc.stepOffset = 0.25f;
            cc.skinWidth = 0.02f;
            cc.minMoveDistance = 0f;

            PlayerGameObject = view.gameObject;
            PlayerCharacterController = cc;
            _isPlayerSpawned = true;

            Debug.Log($"[EntitySpawner] Player entity upgraded: {view.gameObject.name}" +
                      (PlayerAnimator != null ? " (with Animator)" : ""));
        }

        /// <summary>
        /// Removes StarterAssets scripts from the instantiated player visual so they don't
        /// fight our PlayerInputController for movement, look, jump, etc.
        /// Looked up by full type name to avoid an asmdef dependency on StarterAssets.
        /// </summary>
        private static void StripStarterAssetsScripts(GameObject root)
        {
            string[] typeNames =
            {
                "StarterAssets.ThirdPersonController",
                "StarterAssets.BasicRigidBodyPush",
                "StarterAssets.StarterAssetsInputs",
                "UnityEngine.InputSystem.PlayerInput",
            };

            foreach (string typeName in typeNames)
            {
                System.Type t = FindType(typeName);

                if (t == null)
                    continue;

                Component[] comps = root.GetComponentsInChildren(t, includeInactive: true);

                foreach (Component c in comps)
                {
                    if (c != null)
                    {
                        Object.Destroy(c);
                    }
                }
            }
        }

        /// <summary>
        /// The PlayerArmature prefab ships with a CharacterController on its root.
        /// We add our own on the entity root, so strip the prefab's one to avoid duplicates.
        /// </summary>
        private static void StripCharacterControllerOnVisual(GameObject visual)
        {
            CharacterController cc = visual.GetComponent<CharacterController>();

            if (cc != null)
            {
                Object.Destroy(cc);
            }
        }

        private static System.Type FindType(string fullName)
        {
            foreach (System.Reflection.Assembly asm in System.AppDomain.CurrentDomain.GetAssemblies())
            {
                System.Type t = asm.GetType(fullName, throwOnError: false);

                if (t != null)
                    return t;
            }

            return null;
        }

        private static void SetColor(GameObject go, Color color)
        {
            Renderer renderer = go.GetComponent<Renderer>();

            if (renderer != null)
            {
                renderer.material.color = color;
            }
        }
    }
}

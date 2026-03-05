using System.Collections.Generic;
using Core.Movement;
using Game.State;
using UnityEngine;

namespace Game.Entity
{
    /// <summary>
    /// Listens to WorldState entity updates and instantiates/manages GameObjects in the scene.
    /// Creates a basic capsule for each entity on first spawn. When the controlled player entity
    /// is identified, upgrades it with a CharacterController for input-driven movement.
    /// </summary>
    public class EntitySpawner
    {
        private readonly WorldState _worldState;
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

        public EntitySpawner(WorldState worldState)
        {
            _worldState = worldState;
            _worldState.EntityUpdated += OnEntityUpdated;
        }

        public void Dispose()
        {
            _worldState.EntityUpdated -= OnEntityUpdated;
        }

        /// <summary>
        /// Called each frame to check if the player entity needs upgrading from a basic NPC
        /// capsule to a player-controlled entity (with CharacterController, no EntityMotion).
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

            // Attach EntityMotion for smooth interpolation (NPCs)
            EntityMotion motion = go.AddComponent<EntityMotion>();
            motion.Initialize(entity);

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

            // Remove the CapsuleCollider on the child visual — it conflicts with CharacterController
            Transform visualChild = view.transform.Find("Visual");

            if (visualChild != null)
            {
                CapsuleCollider childCollider = visualChild.GetComponent<CapsuleCollider>();

                if (childCollider != null)
                {
                    Object.Destroy(childCollider);
                }
            }

            // Add CharacterController on the root GO.
            // center = (0, 1, 0) matches the child capsule mesh offset, so the physics
            // collider aligns with the visual. Root transform.y = 0 means "on the ground".
            CharacterController cc = view.gameObject.AddComponent<CharacterController>();
            cc.height = 2f;
            cc.radius = 0.5f;
            cc.center = new Vector3(0f, 1f, 0f);

            PlayerGameObject = view.gameObject;
            PlayerCharacterController = cc;
            _isPlayerSpawned = true;

            Debug.Log($"[EntitySpawner] Player entity upgraded: {view.gameObject.name}");
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

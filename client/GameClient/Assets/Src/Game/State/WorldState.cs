using System;
using System.Collections.Generic;
using Game.Entity;
using Game.Entity.Components;
using UnityEngine;

namespace Game.State
{
    /// <summary>
    /// Central game state container. Tracks all world entities, the controlled player,
    /// and exposes C# events for the view layer to subscribe to.
    ///
    /// Thread-safety: entity mutations happen from packet handlers (which run on the main thread
    /// via GatewayConnection.ProcessIncomingPackets), so no concurrent access issues.
    /// </summary>
    public class WorldState
    {
        /// <summary>
        /// The GUID of the player entity controlled by this client. Null until login completes.
        /// </summary>
        public ObjectGuid ControlledEntityId { get; private set; }

        /// <summary>
        /// Fired when any entity is spawned, moved, or has a resource change.
        /// </summary>
        public event Action<WorldEntityUpdate> EntityUpdated;

        /// <summary>
        /// Fired when the server sends a debug signal.
        /// </summary>
        public event Action<DebugSignalEvent> DebugSignalReceived;

        private readonly Dictionary<ulong, WorldEntity> _entities = new Dictionary<ulong, WorldEntity>();

        /// <summary>
        /// Adds the local player to the world and sets them as the controlled entity.
        /// </summary>
        public void AddPlayerToWorld(PlayerEntity player)
        {
            AddEntity(player);
            ControlledEntityId = player.Guid;
        }

        /// <summary>
        /// Adds a new entity to the world, or updates an existing one in-place to preserve
        /// references held by MonoBehaviours. Fires a Spawn update in both cases.
        /// </summary>
        public void AddEntity(WorldEntity entity)
        {
            ulong key = entity.Guid.GetRawValue();

            if (_entities.TryGetValue(key, out WorldEntity existing))
            {
                existing.Position = entity.Position;
                existing.Orientation = entity.Orientation;
            }
            else
            {
                _entities[key] = entity;
            }

            EntityUpdated?.Invoke(new WorldEntityUpdate(WorldUpdateType.Spawn, _entities[key]));
        }

        /// <summary>
        /// Updates an entity's position, direction, and moving state. Fires a Position update.
        /// </summary>
        public void UpdateEntityPosition(
            ObjectGuid guid,
            Vector3 position,
            float orientation,
            Vector3 direction,
            bool isMoving)
        {
            WorldEntity entity = GetEntityById(guid);

            if (entity == null)
                return;

            entity.Position = position;
            entity.Orientation = orientation;
            entity.Direction = direction;
            entity.IsMoving = isMoving;
            EntityUpdated?.Invoke(new WorldEntityUpdate(WorldUpdateType.Position, entity));
        }

        /// <summary>
        /// Updates an entity's health. Adds a HealthComponent if the entity doesn't have one yet
        /// (receiving a health update from the server implies the entity has health).
        /// Fires a Resource update.
        /// </summary>
        public void UpdateEntityHealth(ObjectGuid guid, uint newHealth)
        {
            WorldEntity entity = GetEntityById(guid);

            if (entity == null)
                return;

            var health = entity.GetComponent<HealthComponent>();

            if (health == null)
            {
                health = new HealthComponent(newHealth);
                entity.AddComponent(health);
            }
            else
            {
                health.Health = newHealth;
            }

            EntityUpdated?.Invoke(new WorldEntityUpdate(WorldUpdateType.Resource, entity));
        }

        /// <summary>
        /// Sets the last movement destination for an entity (used for NavMesh pathfinding).
        /// </summary>
        public void UpdateEntityDestination(ObjectGuid guid, Vector3 destination)
        {
            WorldEntity entity = GetEntityById(guid);

            if (entity != null)
            {
                entity.IsMoving = true;
                entity.LastRequestedDestination = destination;
            }
        }

        /// <summary>
        /// Dispatches a debug signal event.
        /// </summary>
        public void DispatchDebugSignal(string name, int value)
        {
            DebugSignalReceived?.Invoke(new DebugSignalEvent(name, value));
        }

        public WorldEntity GetEntityById(ObjectGuid guid)
        {
            _entities.TryGetValue(guid.GetRawValue(), out WorldEntity entity);
            return entity;
        }

        public WorldEntity GetControlledEntity()
        {
            if (ControlledEntityId == null)
                return null;

            return GetEntityById(ControlledEntityId);
        }

        public IReadOnlyDictionary<ulong, WorldEntity> GetAllEntities()
        {
            return _entities;
        }
    }
}

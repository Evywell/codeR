using System;
using System.Collections.Generic;
using UnityEngine;

namespace Game.Entity
{
    /// <summary>
    /// Represents any object in the game world.
    /// Uses Unity's native Vector3 for position and direction — Y/Z swap is applied
    /// at the deserialization boundary (in packet handlers), not here.
    ///
    /// Extra data beyond identity and transform is held in components, mirroring
    /// the server's component-based architecture.
    /// </summary>
    public class WorldEntity
    {
        public ObjectGuid Guid { get; private set; }

        /// <summary>
        /// World position in Unity coordinate space (server Y/Z swap applied at deserialization).
        /// </summary>
        public Vector3 Position { get; set; }

        /// <summary>
        /// Orientation in server radians. Conversion to Unity degrees happens at the view layer.
        /// </summary>
        public float Orientation { get; set; }

        public float Speed { get; set; }

        public bool IsMoving { get; set; }

        public Vector3 Direction { get; set; }

        /// <summary>
        /// The last destination the server told this object to move toward (NavMesh pathfinding).
        /// Null if no destination is active.
        /// </summary>
        public Vector3? LastRequestedDestination { get; set; }

        private readonly Dictionary<Type, object> _components = new Dictionary<Type, object>();

        public WorldEntity(ObjectGuid guid, Vector3 position, float orientation)
        {
            Guid = guid;
            Position = position;
            Orientation = orientation;
            Direction = Vector3.zero;
            Speed = 3.5f;
            IsMoving = false;
            LastRequestedDestination = null;
        }

        public WorldEntity(ObjectGuid guid) : this(guid, Vector3.zero, 0f) { }

        /// <summary>
        /// Adds or replaces a component on this entity.
        /// </summary>
        public void AddComponent<T>(T component) where T : class
        {
            _components[typeof(T)] = component;
        }

        /// <summary>
        /// Returns the component of the given type, or null if absent.
        /// </summary>
        public T GetComponent<T>() where T : class
        {
            _components.TryGetValue(typeof(T), out object component);
            return component as T;
        }

        /// <summary>
        /// Returns true if this entity has a component of the given type.
        /// </summary>
        public bool HasComponent<T>() where T : class
        {
            return _components.ContainsKey(typeof(T));
        }

        public override string ToString()
        {
            return $"{GetType().Name} {{ Guid: {Guid}, Position: {Position} }}";
        }
    }
}

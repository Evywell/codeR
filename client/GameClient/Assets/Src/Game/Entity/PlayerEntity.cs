using UnityEngine;

namespace Game.Entity
{
    /// <summary>
    /// Represents a player entity in the world. Extends WorldEntity with a name.
    /// </summary>
    public class PlayerEntity : WorldEntity
    {
        public string Name { get; private set; }

        public PlayerEntity(string name, ObjectGuid guid, Vector3 position, float orientation)
            : base(guid, position, orientation)
        {
            Name = name;
        }
    }
}

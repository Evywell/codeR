using UnityEngine;

namespace Game.Entity
{
    /// <summary>
    /// MonoBehaviour attached to every spawned entity GameObject.
    /// Bridges the WorldEntity data model to the Unity scene.
    /// </summary>
    public class EntityView : MonoBehaviour
    {
        public WorldEntity Entity { get; private set; }

        public void Initialize(WorldEntity entity)
        {
            Entity = entity;
        }

        public ulong GetGuidRawValue()
        {
            return Entity?.Guid.GetRawValue() ?? 0;
        }
    }
}

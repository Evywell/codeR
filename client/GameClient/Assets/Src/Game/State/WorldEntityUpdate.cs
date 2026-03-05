using Game.Entity;

namespace Game.State
{
    /// <summary>
    /// Describes what kind of update occurred on a world entity.
    /// </summary>
    public enum WorldUpdateType
    {
        Spawn,
        Position,
        Resource
    }

    /// <summary>
    /// Represents an update to a world entity, used to notify the view layer.
    /// </summary>
    public readonly struct WorldEntityUpdate
    {
        public WorldUpdateType Type { get; }
        public WorldEntity Entity { get; }

        public WorldEntityUpdate(WorldUpdateType type, WorldEntity entity)
        {
            Type = type;
            Entity = entity;
        }
    }
}

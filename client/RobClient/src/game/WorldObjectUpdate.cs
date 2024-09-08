using RobClient.Game.Entity;

namespace RobClient.Game {
    public readonly struct WorldObjectUpdate
    {
        public WorldObjectUpdate(UpdateType type, WorldObject worldObject) {
            Type = type;
            WorldObject = worldObject;
        }

        public UpdateType Type { get; }
        public WorldObject WorldObject { get; }
    }

    public enum UpdateType {
        SPAWN,
        POSITION,
        RESOURCE,
    }
}
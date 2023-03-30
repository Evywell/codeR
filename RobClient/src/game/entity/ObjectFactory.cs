using RobClient.Game.Entity.Guid;

namespace RobClient.Game.Entity;

public class ObjectFactory {
    public WorldObject Create(ObjectGuid guid, Vector4f position) {
        return new WorldObject(guid, position);
    }

    public Player CreatePlayer(ObjectGuid guid, string name, Vector4f position) {
        return new Player(name, guid, position);
    }
}
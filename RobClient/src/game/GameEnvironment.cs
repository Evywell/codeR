using System.Collections.Concurrent;
using RobClient.Game.Entity.Guid;
using RobClient.Game.Entity;

namespace RobClient.Game;

public class GameEnvironment {
    private ConcurrentDictionary<ulong, WorldObject> _objects = new ConcurrentDictionary<ulong, WorldObject>();

    public void AttachObject(WorldObject worldObject) {
        _objects.TryAdd(worldObject.Guid.GetRawValue(), worldObject);
    }

    public void UpdateObjectPosition(ObjectGuid guid, Vector4f position) {
        WorldObject? worldObject;

        _objects.TryGetValue(guid.GetRawValue(), out worldObject);

        if (worldObject != null) {
            worldObject.Position = position;
        }
    }

    public IReadOnlyDictionary<ulong, WorldObject> GetWorldObjects() {
        return _objects;
    }
}
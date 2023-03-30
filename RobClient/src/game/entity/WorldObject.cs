using RobClient.Game.Entity.Guid;

namespace RobClient.Game.Entity;

public class WorldObject {
    public ObjectGuid Guid
    { get; private set; }

    public Vector4f Position
    { get; set; }

    public WorldObject(ObjectGuid guid, Vector4f position) {
        Guid = guid;
        Position = position;
    }

    public WorldObject(ObjectGuid guid) : this(guid, Vector4f.Zero()) {}

    public override string ToString() {
        return $"Position: {{ X: {Position.X}, Y: {Position.Y}, Z: {Position.Z} }}";
    }
}
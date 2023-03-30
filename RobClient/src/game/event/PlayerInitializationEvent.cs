using RobClient.Event;
using RobClient.Game.Entity;
using RobClient.Game.Entity.Guid;

namespace RobClient.Game.Event;

public class PlayerInitializationEvent : IEvent
{
    public ObjectGuid Guid
    { get; private set; }

    public string Name
    { get; private set; }

    public Vector4f Position
    { get; private set; }

    public PlayerInitializationEvent(ObjectGuid guid, string name, Vector4f position) {
        Guid = guid;
        Name = name;
        Position = position;
    }
}
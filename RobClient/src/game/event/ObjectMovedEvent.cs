using RobClient.Event;
using RobClient.Game.Entity;

namespace RobClient.Game.Event;

public class ObjectMovedEvent : IEvent
{
    public ulong Guid
    { get; private set; }

    public Vector3f Position
    { get; private set; }

    public float Orientation
    { get; private set; }

    public float Speed
    { get; private set; }

    public ObjectMovedEvent(ulong guid, Vector3f position, float orientation, float speed) {
        Guid = guid;
        Position = position;
        Orientation = orientation;
        Speed = speed;
    }
}
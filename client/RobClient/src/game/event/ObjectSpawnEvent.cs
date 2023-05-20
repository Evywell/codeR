using RobClient.Event;
using RobClient.Game.Entity;

namespace RobClient.Game.Event {
    public class ObjectSpawnEvent : IEvent
    {
        public ulong Guid
        { get; private set; }

        public Vector3f Position
        { get; private set; }

        public float Orientation
        { get; private set; }

        public ObjectSpawnEvent(ulong guid, Vector3f position, float orientation) {
            Guid = guid;
            Position = position;
            Orientation = orientation;
        }
    }
}

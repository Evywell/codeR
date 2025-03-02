using System.Collections.Generic;
using Fr.Raven.Proto.Message.Game;
using RobClient.Game.Entity.Guid;

namespace RobClient.Game.Entity {
    public class WorldObject {
        public ObjectGuid Guid
        { get; private set; }

        public Vector4f Position
        { get; set; }

        public float Speed
        { get; set; }

        public bool IsMoving
        { get; set; }

        public Vector3f Direction
        { get; set; }

        public uint Health
        { get; set; }

        public Vector3f LastRequestedMovementDestination
        { get; set; }

        public WorldObject(ObjectGuid guid, Vector4f position) {
            Guid = guid;
            Position = position;
            Direction = new Vector3f(0, 0, 0);
            Speed = 3.0f;
            Health = 100;
            IsMoving = false;
        }

        public WorldObject(ObjectGuid guid) : this(guid, Vector4f.Zero()) {}

        public override string ToString() {
            var className = this.GetType().Name;
            return $"{className} {{ Guid: {Guid.GetRawValue()}, Position: {{ X: {Position.X}, Y: {Position.Y}, Z: {Position.Z} }} }}";
        }
    }
}

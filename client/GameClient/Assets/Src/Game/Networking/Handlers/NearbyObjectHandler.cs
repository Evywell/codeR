using Core.Networking.Routing;
using Fr.Raven.Proto.Message.Game;
using Game.Entity;
using Game.State;
using Google.Protobuf;
using UnityEngine;

namespace Game.Networking.Handlers
{
    /// <summary>
    /// Handles SMSG_NEARBY_OBJECT_UPDATE (0x03).
    /// Creates a new world entity for a nearby object and adds it to the world.
    /// </summary>
    public class NearbyObjectHandler : IPacketHandler
    {
        private readonly WorldState _worldState;

        public NearbyObjectHandler(WorldState worldState)
        {
            _worldState = worldState;
        }

        public void Handle(ByteString body)
        {
            NearbyObjectOpcode nearbyObject = NearbyObjectOpcode.Parser.ParseFrom(body);

            // Server sends Y/Z swapped relative to Unity: server(X,Y,Z) -> Unity(X,Z,Y)
            var entity = new WorldEntity(
                ObjectGuid.From(nearbyObject.Guid),
                new Vector3(nearbyObject.PosX, nearbyObject.PosZ, nearbyObject.PosY),
                nearbyObject.Orientation
            );

            _worldState.AddEntity(entity);
        }
    }
}

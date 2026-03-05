using Core.Networking.Routing;
using Fr.Raven.Proto.Message.Game;
using Game.Entity;
using Game.State;
using Google.Protobuf;
using UnityEngine;

namespace Game.Networking.Handlers
{
    /// <summary>
    /// Handles SMSG_OBJECT_MOVING_TO_DESTINATION (0x0A).
    /// Sets the entity's destination for pathfinding.
    /// </summary>
    public class ObjectDestinationHandler : IPacketHandler
    {
        private readonly WorldState _worldState;

        public ObjectDestinationHandler(WorldState worldState)
        {
            _worldState = worldState;
        }

        public void Handle(ByteString body)
        {
            ObjectMovingToDestination msg = ObjectMovingToDestination.Parser.ParseFrom(body);

            // Waypoints arrive in Unity space (PhysicServer sends raw Vec3 from
            // ObjectController.SendMovement, game server relays without swap).
            // No Y/Z swap needed here — unlike heartbeats which go through
            // TransformSyncOverNetwork and ARE in server convention.
            var destination = new Vector3(
                msg.Destination.PosX,
                msg.Destination.PosY,
                msg.Destination.PosZ
            );

            _worldState.UpdateEntityDestination(
                ObjectGuid.From(msg.Guid),
                destination
            );
        }
    }
}

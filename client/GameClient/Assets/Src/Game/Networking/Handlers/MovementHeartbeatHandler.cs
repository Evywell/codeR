using Core.Networking.Protocol;
using Core.Networking.Routing;
using Fr.Raven.Proto.Message.Game;
using Game.Entity;
using Game.State;
using Google.Protobuf;
using UnityEngine;

namespace Game.Networking.Handlers
{
    /// <summary>
    /// Handles SMSG_MOVEMENT_HEARTBEAT (0x04).
    /// Updates an entity's position and movement state.
    /// </summary>
    public class MovementHeartbeatHandler : IPacketHandler
    {
        private readonly WorldState _worldState;

        public int Opcode => Opcodes.SMSG_MOVEMENT_HEARTBEAT;

        public MovementHeartbeatHandler(WorldState worldState)
        {
            _worldState = worldState;
        }

        public void Handle(ByteString body)
        {
            MovementHeartbeat heartbeat = MovementHeartbeat.Parser.ParseFrom(body);

            // Server sends Y/Z swapped: server(X,Y,Z) -> Unity(X,Z,Y)
            var position = new Vector3(
                heartbeat.Position.PosX,
                heartbeat.Position.PosZ,
                heartbeat.Position.PosY
            );

            var direction = new Vector3(
                heartbeat.Direction.X,
                heartbeat.Direction.Z,
                heartbeat.Direction.Y
            );

            bool isMoving = heartbeat.Phase == MovementPhase.PhaseBegin;

            _worldState.UpdateEntityPosition(
                ObjectGuid.From(heartbeat.Guid),
                position,
                heartbeat.Position.Orientation,
                direction,
                isMoving,
                heartbeat.Speed
            );
        }
    }
}

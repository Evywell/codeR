using Core.Networking.Gateway;
using Fr.Raven.Proto.Message.Game;
using Core.Networking.Protocol;
using UnityEngine;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;

namespace Game.Interaction
{
    /// <summary>
    /// Sends player movement data to the server.
    /// Constructs the SMovementInfo protobuf message from Unity world-space coordinates.
    /// </summary>
    public class MovementSender
    {
        private readonly IPacketSender _sender;

        public MovementSender(IPacketSender sender)
        {
            _sender = sender;
        }

        public void SendInProgressMovement(float posX, float posY, float posZ, float orientation, Vector3 direction)
        {
            Send(posX, posY, posZ, orientation, direction, MovementPhase.PhaseBegin);
        }

        public void SendStoppedMovement(float posX, float posY, float posZ, float orientation, Vector3 direction)
        {
            Send(posX, posY, posZ, orientation, direction, MovementPhase.PhaseEnd);
        }

        /// <summary>
        /// Sends the player's current position, orientation, and movement direction to the server.
        /// Coordinates should already be in server space (Unity Y/Z swapped).
        /// </summary>
        public void Send(float posX, float posY, float posZ, float orientation, Vector3 direction, MovementPhase phase)
        {
            var position = new Position
            {
                PosX = posX,
                PosY = posY,
                PosZ = posZ,
                Orientation = orientation
            };

            var movement = new SMovementInfo
            {
                Phase = phase,
                Position = position,
                Direction = new Direction
                {
                    X = direction.x,
                    Y = direction.y,
                    Z = direction.z
                }
            };

            _sender.SendMessage(
                Opcodes.CMSG_PLAYER_MOVEMENT,
                movement,
                GatewayPacket.Types.Context.Game
            );
        }
    }
}

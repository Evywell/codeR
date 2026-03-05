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
    /// Handles SMSG_PLAYER_DESCRIPTION (0x02).
    /// Creates the local player entity and adds it to the world state.
    /// </summary>
    public class PlayerDescriptionHandler : IPacketHandler
    {
        private readonly WorldState _worldState;

        public int Opcode => Opcodes.SMSG_PLAYER_DESCRIPTION;

        public PlayerDescriptionHandler(WorldState worldState)
        {
            _worldState = worldState;
        }

        public void Handle(ByteString body)
        {
            PlayerDescription description = PlayerDescription.Parser.ParseFrom(body);

            // TODO: use real position from server when available
            var player = new PlayerEntity(
                description.Name,
                ObjectGuid.From(description.Guid),
                new Vector3(12f, 0.7f, 0f),
                2.61799f
            );

            _worldState.AddPlayerToWorld(player);
        }
    }
}

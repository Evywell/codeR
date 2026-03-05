using Core.Networking.Protocol;
using Core.Networking.Routing;
using Fr.Raven.Proto.Message.Game;
using Game.Entity;
using Game.State;
using Google.Protobuf;

namespace Game.Networking.Handlers
{
    /// <summary>
    /// Handles SMSG_OBJECT_HEALTH_UPDATED (0x08).
    /// Updates an entity's health value.
    /// </summary>
    public class ObjectHealthHandler : IPacketHandler
    {
        private readonly WorldState _worldState;

        public int Opcode => Opcodes.SMSG_OBJECT_HEALTH_UPDATED;

        public ObjectHealthHandler(WorldState worldState)
        {
            _worldState = worldState;
        }

        public void Handle(ByteString body)
        {
            ObjectSheetUpdate update = ObjectSheetUpdate.Parser.ParseFrom(body);

            _worldState.UpdateEntityHealth(
                ObjectGuid.From(update.Guid),
                update.Health
            );
        }
    }
}

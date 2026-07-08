using Core.Networking.Protocol;
using Core.Networking.Routing;
using Fr.Raven.Proto.Message.Game;
using Game.Ability;
using Game.State;
using Google.Protobuf;
using UnityEngine;

namespace Game.Networking.Handlers
{
    /// <summary>
    /// Handles SMSG_ABILITY_STATE_UPDATE (0x0C).
    /// Logs ability state changes for any state.
    /// </summary>
    public class AbilityStateUpdateHandler : IPacketHandler
    {
        public int Opcode => Opcodes.SMSG_ABILITY_STATE_UPDATE;

        private readonly WorldState _worldState;

        public AbilityStateUpdateHandler(WorldState worldState)
        {
            _worldState = worldState;
        }

        public void Handle(ByteString body)
        {
            AbilityStateUpdate update = AbilityStateUpdate.Parser.ParseFrom(body);

            _worldState.UpdateAbilityState(update);

            Debug.Log($"[Ability] Entity {update.SourceGuid} ability {update.AbilityId} state: {update.AbilityState}");
        }
    }
}

using Core.Networking.Gateway;
using Fr.Raven.Proto.Message.Game;
using Game.Entity;
using Core.Networking.Protocol;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;

namespace Game.Interaction
{
    public class AbilityPerformer
    {
        private readonly IPacketSender _sender;

        public AbilityPerformer(IPacketSender sender)
        {
            _sender = sender;
        }

        public void UseAbility(uint abilityInfoId, ObjectGuid target)
        {
            var message = new UseAbility
            {
                AbilityId = abilityInfoId,
                ExplicitTargetGuid = target.GetRawValue()
            };

            _sender.SendMessage(
                Opcodes.CMSG_PLAYER_USE_ABILITY,
                message,
                GatewayPacket.Types.Context.Game
            );
        }
    }
}
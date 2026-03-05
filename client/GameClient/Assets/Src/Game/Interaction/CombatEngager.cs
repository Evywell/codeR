using Core.Networking.Gateway;
using Fr.Raven.Proto.Message.Game;
using Game.Entity;
using Core.Networking.Protocol;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;

namespace Game.Interaction
{
    /// <summary>
    /// Sends combat engagement requests to the server.
    /// </summary>
    public class CombatEngager
    {
        private readonly IPacketSender _sender;

        public CombatEngager(IPacketSender sender)
        {
            _sender = sender;
        }

        public void EngageCombat(ObjectGuid target)
        {
            var message = new PlayerEngageCombat
            {
                Target = target.GetRawValue()
            };

            _sender.SendMessage(
                Opcodes.CMSG_PLAYER_ENGAGE_COMBAT,
                message,
                GatewayPacket.Types.Context.Game
            );
        }
    }
}

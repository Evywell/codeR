using Core.Networking.Gateway;
using Fr.Raven.Proto.Message.Game;
using Core.Networking.Protocol;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;

namespace Game.Interaction
{
    /// <summary>
    /// Sends spell cast requests to the server.
    /// </summary>
    public class SpellCaster
    {
        private readonly IPacketSender _sender;

        public SpellCaster(IPacketSender sender)
        {
            _sender = sender;
        }

        public void CastSpell(uint spellId)
        {
            var message = new CastSpell
            {
                SpellId = spellId
            };

            _sender.SendMessage(
                Opcodes.CMSG_PLAYER_CAST_SPELL,
                message,
                GatewayPacket.Types.Context.Game
            );
        }
    }
}

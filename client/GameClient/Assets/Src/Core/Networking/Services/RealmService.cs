using System.Threading.Tasks;
using Core.Networking.Gateway;
using Core.Networking.Protocol;
using Fr.Raven.Proto.Message.Game.Setup;
using Fr.Raven.Proto.Message.Gateway;
using Fr.Raven.Proto.Message.Realm;

namespace Core.Networking.Services
{
    /// <summary>
    /// Handles realm operations: joining the world with a character.
    /// </summary>
    public class RealmService
    {
        private readonly IPacketSender _sender;

        public RealmService(IPacketSender sender)
        {
            _sender = sender;
        }

        public async Task JoinWorldWithCharacter(int characterId)
        {
            var joinTheWorld = new JoinTheWorld
            {
                CharacterId = (uint)characterId
            };

            var logIntoWorld = new LogIntoWorld
            {
                CharacterId = (uint)characterId
            };

            await _sender.SendMessage(
                Opcodes.CMSG_REALM_JOIN_WORLD,
                joinTheWorld,
                Packet.Types.Context.Realm
            );

            await _sender.SendMessage(
                Opcodes.CMSG_LOG_INTO_WORLD,
                logIntoWorld,
                Packet.Types.Context.Game
            );
        }
    }
}

using System.Threading.Tasks;
using Fr.Raven.Proto.Message.Game.Setup;
using Fr.Raven.Proto.Message.Gateway;
using Fr.Raven.Proto.Message.Realm;
using RobClient.Network;

namespace RobClient.Game.Realm {
    public class RealmApi
    {
        private IMessageSender _sender;

        public RealmApi(IMessageSender sender) {
            _sender = sender;
        }

        public async Task JoinWorldWithCharacter(int characterId)
        {
            var joinTheWorld = new JoinTheWorld();
            joinTheWorld.CharacterId = (uint)characterId;

            await _sender.SendMessage(0x03, joinTheWorld, Packet.Types.Context.Realm);
            await _sender.SendMessage(0x01, new LogIntoWorld(), Packet.Types.Context.Game);
        }
    }
}
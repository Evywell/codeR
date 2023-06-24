using System.Threading.Tasks;
using Fr.Raven.Proto.Message.Eas;
using Fr.Raven.Proto.Message.Gateway;
using RobClient.Game;
using RobClient.Game.Interaction;
using RobClient.Game.Realm;
using RobClient.Network;

namespace RobClient {
    public class GameClient {
        private IMessageSender _sender;
        public RealmApi Realm
        { get; private set; }

        public GameEnvironment Game
        { get; private set; }

        public PlayerInteraction Interaction
        { get; private set; }

        public GameClient(
            GameEnvironment game,
            PlayerInteraction playerInteraction,
            IMessageSender sender
        ) {
            _sender = sender;
            Game = game;
            Interaction = playerInteraction;
            Realm = new RealmApi(sender);
        }

        public async Task AuthenticateWithUserId(int userId)
        {
            var devAuthPacket = new DevAuthenticationPacket();
            devAuthPacket.UserId = (uint)userId;
            
            var authMessage = new EasPacket();
            authMessage.AuthType = EasPacket.Types.Type.Dev;
            authMessage.DevAuthPacket = devAuthPacket;

            await _sender.SendMessage(0, authMessage, Packet.Types.Context.Eas);
        } 
    }
}
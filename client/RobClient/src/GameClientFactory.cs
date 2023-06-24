using RobClient.Game;
using RobClient.Game.Interaction;
using RobClient.Game.World;
using RobClient.Network;

namespace RobClient {
    public class GameClientFactory {
        public GameClient Create(IMessageSender sender, IPacketReceiver receiver)
        {
            var worldApi = new WorldApi(sender, receiver);
            var gameEnvironment = new GameEnvironment();
            var playerInteraction = new PlayerInteraction(gameEnvironment, sender);

            // Init the server's hook
            new WorldObserver(gameEnvironment, worldApi);

            return new GameClient(gameEnvironment, playerInteraction, sender);
        }
    }
}
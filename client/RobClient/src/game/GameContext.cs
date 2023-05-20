using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using LogIntoWorldOpcode = Fr.Raven.Proto.Message.Game.Setup.LogIntoWorld;
using Google.Protobuf;
using RobClient.Event;
using RobClient.Game.Event;
using RobClient.Game.Entity;
using RobClient.Game.Interaction;
using RobClient.Opcode;
using RobClient.Game.Opcode;
using RobClient.Game.View;
using System.Threading.Tasks;
using RobClient.Game.World;

namespace RobClient.Game {
    public class GameContext {

        public GameEnvironment GameEnvironment
        { get; private set; }

        public ViewEnvironment ViewEnvironment
        { get; private set; }

        public PlayerInteraction PlayerInteraction
        { get; private set; }

        private Client _client;
        public EventDispatcher EventDispatcher
        { get; private set; }

        internal GameContext(Client client, LocalWorld world, IObjectViewFactory objectViewFactory) {
            _client = client;
            EventDispatcher = new EventDispatcher();
            GameEnvironment = new GameEnvironment();
            ViewEnvironment = new ViewEnvironment(objectViewFactory);
            PlayerInteraction = new PlayerInteraction(_client, world);

            var opcodeManager = new OpcodeManager();
            opcodeManager.AttachHandler(0x02, new PlayerDescriptionHandler(this));
            opcodeManager.AttachHandler(0x03, new NearbyObjectHandler(this));
            opcodeManager.AttachHandler(0x04, new MovementHeartbeatHandler(this));

            InitEventHandlers();

            _client.AddOnPackedReceivedListener(packet => {
                if (packet.Context != GatewayPacket.Types.Context.Game) {
                    return false;
                }

                opcodeManager.HandleOpcode(packet.Opcode, packet);

                return true;
            });
        }

        public async Task JoinWorld() {
            var packet = new GatewayPacket();
            packet.Opcode = 0x01;
            packet.Context = GatewayPacket.Types.Context.Game;
            packet.Body = (new LogIntoWorldOpcode()).ToByteString();

            await _client.Send(packet);
        }

        public void DispatchEvent(IEvent sourceEvent) {
            EventDispatcher.Dispatch(sourceEvent);
        }

        private void InitEventHandlers() {
            var objectFactory = new ObjectFactory();

            EventDispatcher.AddEventHandler(new PlayerInitializationEventHandler(objectFactory, GameEnvironment));
            EventDispatcher.AddEventHandler(new ObjectSpawnEventHandler(objectFactory, GameEnvironment, ViewEnvironment));
            EventDispatcher.AddEventHandler(new ObjectMovedEventHandler(GameEnvironment));
        }
    }
}

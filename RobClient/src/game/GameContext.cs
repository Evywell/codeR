using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using LogIntoWorldOpcode = Fr.Raven.Proto.Message.Game.Setup.LogIntoWorld;
using Google.Protobuf;
using RobClient.Event;
using RobClient.Game.Event;
using RobClient.Game.Entity;
using RobClient.Opcode;
using RobClient.Game.Opcode;
using RobClient.Game.View;

namespace RobClient.Game;

public class GameContext {

    public GameEnvironment GameEnvironment
    { get; private set; }

    public ViewEnvironment ViewEnvironment
    { get; private set; }

    private Client _client;
    private EventDispatcher _eventDispatcher;

    internal GameContext(Client client, IObjectViewFactory objectViewFactory) {
        _client = client;
        _eventDispatcher = new EventDispatcher();
        GameEnvironment = new GameEnvironment();
        ViewEnvironment = new ViewEnvironment(objectViewFactory);

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
        _eventDispatcher.Dispatch(sourceEvent);
    }

    private void InitEventHandlers() {
        var objectFactory = new ObjectFactory();

        _eventDispatcher.AddEventHandler(new PlayerInitializationEventHandler(objectFactory, GameEnvironment));
        _eventDispatcher.AddEventHandler(new ObjectSpawnEventHandler(objectFactory, GameEnvironment, ViewEnvironment));
        _eventDispatcher.AddEventHandler(new ObjectMovedEventHandler(GameEnvironment));
    }
}
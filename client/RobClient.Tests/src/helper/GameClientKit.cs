using RobClient.Network;

namespace RobClient.Tests.Helper;

public class GameClientKit {
    public GameClient Client
    { get; private set; }

    public SimplePacketReceiver Receiver
    { get; private set; }

    public GameClientKit(GameClient client, SimplePacketReceiver receiver)
    {
        Client = client;
        Receiver = receiver;
    }

    public static GameClientKit Create()
    {
        var factory = new GameClientFactory();
        var sender = new VoidMessageSender();
        var receiver = new SimplePacketReceiver();

        return new GameClientKit(factory.Create(sender, receiver), receiver);
    }
}
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using Fr.Raven.Proto.Message.Game;
using Google.Protobuf;
using RobClient.Game.Entity;
using RobClient.Tests.Helper;
using RobClient.Game;

namespace RobClient.Tests.Game.Interaction;

public class MovementTest
{
    [Test]
    public void PlayerPositionChangedByServerWhenMoving()
    {
        // Arrange
        var kit = GameClientKit.Create();
        var gameClient = kit.Client;
        var receiver = kit.Receiver;
        var objectFactory = new ObjectFactory();
        var player = new PlayerGenerator().GenerateWith("Evywell");

        gameClient.Game.AddPlayerToWorld(player);

        // Act
        var heartbeat = new MovementHeartbeat();
        heartbeat.Guid = player.Guid.GetRawValue();
        heartbeat.Position = new Position();
        heartbeat.Position.PosX = 3;
        heartbeat.Position.PosY = 0.3f;
        heartbeat.Position.PosZ = 0;
        heartbeat.Position.Orientation = 0;

        receiver.OnNext(createGamePacket(0x04, heartbeat));

        // Assert
        Assert.That(player.Position.X, Is.EqualTo(3));
        Assert.That(player.Position.Y, Is.EqualTo(0.3f));
        Assert.That(player.Position.Z, Is.EqualTo(0));
        Assert.That(player.Position.O, Is.EqualTo(0));
    }

    [Test]
    public void PlayerPositionUpdated()
    {
        // Arrange
        var game = new GameEnvironment();
        var playerGenerator = new PlayerGenerator();

        var player1 = playerGenerator.GenerateWith("Evywell");
        game.AddPlayerToWorld(player1);

        var player2 = playerGenerator.GenerateWith("TheRoxor");
        game.AddPlayerToWorld(player2);

        // Act
        game.UpdateObjectPosition(player1.Guid, new Vector4f(4, 4.33f, -1, 0.12f));
        game.UpdateObjectPosition(player2.Guid, new Vector4f(156, -6.3f, 30, 0.4f));

        // Assert
        Assert.That(player1.Position.X, Is.EqualTo(4));
        Assert.That(player1.Position.Y, Is.EqualTo(4.33f));
        Assert.That(player1.Position.Z, Is.EqualTo(-1));
        Assert.That(player1.Position.O, Is.EqualTo(0.12f));

        Assert.That(player2.Position.X, Is.EqualTo(156));
        Assert.That(player2.Position.Y, Is.EqualTo(-6.3f));
        Assert.That(player2.Position.Z, Is.EqualTo(30));
        Assert.That(player2.Position.O, Is.EqualTo(0.4f));
    }

    private GatewayPacket createGamePacket(int opcode, IMessage body)
    {
        var packet = new GatewayPacket();
        packet.Opcode = opcode;
        packet.Context = GatewayPacket.Types.Context.Game;
        packet.Body = body.ToByteString();

        return packet;
    }
}
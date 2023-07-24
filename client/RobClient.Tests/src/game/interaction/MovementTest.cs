using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using Fr.Raven.Proto.Message.Game;
using Google.Protobuf;
using RobClient.Game.Entity;
using RobClient.Tests.Helper;
using RobClient.Game;
using RobClient.Game.Interaction;

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

    [TestCaseSource(nameof(PlayerMovingOnUpdatesCases))]
    public void PlayerMovingOnUpdates(Vector4f originalPosition, float speed, Vector4f expectedPosition, int timeElapsedMs)
    {
        // Arrange
        var game = new GameEnvironment();
        var sender = new VoidMessageSender();
        var playerInteraction = new PlayerInteraction(game, sender);
        var playerGenerator = new PlayerGenerator();
        var player = playerGenerator.GenerateWith("Evywell");
        player.Position = originalPosition;
        player.Speed = speed;
        game.AddPlayerToWorld(player);

        // Act
        playerInteraction.Move(originalPosition.O);
        game.Update(timeElapsedMs);

        // Assert
        // Static speed of 3 m/s
        Assert.That(player.Position.X, Is.EqualTo(expectedPosition.X));
        Assert.That(player.Position.Y, Is.EqualTo(expectedPosition.Y));
        Assert.That(player.Position.Z, Is.EqualTo(expectedPosition.Z));
        Assert.That(player.Position.O, Is.EqualTo(expectedPosition.O));
    }

    public static object[] PlayerMovingOnUpdatesCases =
    {
        // Go full EAST from origin
        new object[] { OriginPosition(), 3.0f, new Vector4f(3f, 0f, 0f, 0f), 1000 },
        // Go full EAST not from origin
        new object[] { new Vector4f(3f, 0f, 0f, 0f), 3.0f, new Vector4f(6f, 0f, 0f, 0f), 1000 },
        // Go full NORTH from origin
        new object[] { OriginPosition(90), 3.0f, new Vector4f(0f, 3f, 0f, 90f), 1000 },
        // Go NORTH/EAST from origin
        new object[] { OriginPosition(45), 3.0f, new Vector4f(2.121f, 2.121f, 0f, 45f), 1000 },
        // Go full SOUTH from origin (-0.003f because of radians rounding)
        new object[] { OriginPosition(270), 3.0f, new Vector4f(0f, -3f, 0f, 270), 1000 },
        // Go full SOUTH not from origin
        new object[] { new Vector4f(7f, 4f, 0f, 270f), 3.0f, new Vector4f(7f, 1f, 0f, 270), 1000 },
        // Go full SOUTH not from origin
        new object[] { new Vector4f(7f, -140f, 0f, 270f), 28f, new Vector4f(6.998f, -168f, 0f, 270f), 1000 },
    };

    [Test]
    public void MovementTriggersUpdate()
    {
        // Arrange
        var game = new GameEnvironment();
        var sender = new VoidMessageSender();
        var playerInteraction = new PlayerInteraction(game, sender);
        var playerGenerator = new PlayerGenerator();
        var player = playerGenerator.GenerateWith("Evywell");
        game.AddPlayerToWorld(player);

        Player? lastUpdatedWorldObject = null;
        game.WorldObjectUpdatedSub.Subscribe((worldObject) => {
            lastUpdatedWorldObject = (Player)worldObject;
        });

        // Act
        playerInteraction.Move(0);

        game.Update(1000);

        // Assert
        Assert.IsNotNull(lastUpdatedWorldObject);
        Assert.That(lastUpdatedWorldObject.Name, Is.EqualTo("Evywell"));
        Assert.That(lastUpdatedWorldObject.Position.X, Is.EqualTo(3.0f));
    }

    private static Vector4f OriginPosition(float orientation = 0f)
    {
        var position = Vector4f.Zero();
        position.O = orientation;

        return position;
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
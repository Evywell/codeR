using Fr.Raven.Proto.Message.Game;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using RobClient.Game.Entity.Guid;
using RobClient.Network;
using Google.Protobuf;
using RobClient.Game.Entity;

namespace RobClient.Tests.Game;

public class GameEnvironmentTest {
    [Test]
    public void PlayerObjectShouldBeAvailableWhenJoiningWorld()
    {
        // Arrange
        var sender = new VoidMessageSender();
        var receiver = new SimplePacketReceiver();
        var gameClient = (new GameClientFactory()).Create(sender, receiver);
        var playerGuid = new ObjectGuid(new LowGuid(0, 1), (int)GuidType.Player);

        var playerDescription = new PlayerDescription();
        playerDescription.Guid = playerGuid.GetRawValue();
        playerDescription.Name = "Evywell";

        // Act
        receiver.OnNext(createGamePacket(0x02, playerDescription));

        // Assert
        Assert.That(playerGuid.GetRawValue(), Is.EqualTo(gameClient.Game.ControlledObjectId.GetRawValue()));
        Assert.IsInstanceOf(typeof(Player), gameClient.Game.GetControlledObject());
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
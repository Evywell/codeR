using Fr.Raven.Proto.Message.Game;
using Google.Protobuf;
using RobClient.Game.Entity;
using RobClient.Game.Entity.Guid;
using RobClient.Game.Event;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;

namespace RobClient.Game.Opcode;

public class PlayerDescriptionHandler : AbstractGameOpcodeHandler
{
    public PlayerDescriptionHandler(GameContext _context) : base(_context) {}

    public void Call(PlayerDescription message, GatewayPacket packet)
    {
        GameContext.DispatchEvent(new PlayerInitializationEvent(
            ObjectGuid.From(message.Guid), 
            message.Name,
            Vector4f.Zero() // @todo change this by real value
        ));
    }

    public override void Call(IMessage message, GatewayPacket packet)
    {
        Call((PlayerDescription)message, packet);
    }

    public override PlayerDescription GetParsedMessage(ByteString body)
    {
        return PlayerDescription.Parser.ParseFrom(body);
    }
}
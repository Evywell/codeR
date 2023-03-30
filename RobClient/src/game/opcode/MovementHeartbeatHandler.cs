using Fr.Raven.Proto.Message.Game;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using Google.Protobuf;
using RobClient.Game.Event;

namespace RobClient.Game.Opcode;

public class MovementHeartbeatHandler : AbstractGameOpcodeHandler
{
    public MovementHeartbeatHandler(GameContext _context) : base(_context) {}

    public void Call(MovementHeartbeat message, GatewayPacket packet)
    {
        GameContext.DispatchEvent(new ObjectMovedEvent(
            message.Guid,
            new Entity.Vector3f(message.Position.PosX, message.Position.PosY, message.Position.PosZ),
            message.Position.Orientation,
            message.Speed
        ));
    }

    public override void Call(IMessage message, GatewayPacket packet)
    {
        Call((MovementHeartbeat) message, packet);
    }

    public override MovementHeartbeat GetParsedMessage(ByteString body)
    {
        return MovementHeartbeat.Parser.ParseFrom(body);
    }
}
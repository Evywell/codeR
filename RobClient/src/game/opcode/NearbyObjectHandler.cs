using Fr.Raven.Proto.Message.Game;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using Google.Protobuf;
using RobClient.Game.Event;

namespace RobClient.Game.Opcode;

public class NearbyObjectHandler : AbstractGameOpcodeHandler
{
    public NearbyObjectHandler(GameContext _context) : base(_context) {}

    public void Call(NearbyObjectOpcode message, GatewayPacket packet)
    {
        GameContext.DispatchEvent(new ObjectSpawnEvent(
            message.Guid, 
            new Entity.Vector3f(message.PosX, message.PosY, message.PosZ), 
            message.Orientation
        ));
    }

    public override void Call(IMessage message, GatewayPacket packet)
    {
        Call((NearbyObjectOpcode) message, packet);
    }

    public override NearbyObjectOpcode GetParsedMessage(ByteString body)
    {
        return NearbyObjectOpcode.Parser.ParseFrom(body);
    }
}
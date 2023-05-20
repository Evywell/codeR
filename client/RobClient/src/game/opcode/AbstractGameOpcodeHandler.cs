using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using Google.Protobuf;
using RobClient.Opcode;

namespace RobClient.Game.Opcode {
    public abstract class AbstractGameOpcodeHandler : IOpcodeHandler
    {
        public GameContext GameContext
        { get; private set; }

        protected AbstractGameOpcodeHandler(GameContext _context) {
            GameContext = _context;
        }

        public abstract void Call(IMessage message, GatewayPacket packet);
        public abstract IMessage GetParsedMessage(ByteString body);
    }
}

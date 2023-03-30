using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using Google.Protobuf;

namespace RobClient.Opcode;

public interface IOpcodeHandler {
    IMessage GetParsedMessage(ByteString body);

    void Call(IMessage message, GatewayPacket packet);
}
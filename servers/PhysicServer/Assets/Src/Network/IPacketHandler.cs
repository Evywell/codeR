using Google.Protobuf;

namespace App.Network {
    public interface IPacketHandler {
        IMessage CreateMessageFromPayload(ByteString payload);
        void HandleOpcode(uint opcode, IMessage message);
    }
}
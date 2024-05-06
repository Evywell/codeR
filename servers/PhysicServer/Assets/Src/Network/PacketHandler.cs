using Google.Protobuf;

namespace App.Network {
    public abstract class PacketHandler<T> : IPacketHandler {
        public abstract IMessage CreateMessageFromPayload(ByteString payload);
        public abstract void HandleOpcode(uint opcode, T message);

        public void HandleOpcode(uint opcode, IMessage message)
        {
            HandleOpcode(opcode, (T)message);
        }
    }
}
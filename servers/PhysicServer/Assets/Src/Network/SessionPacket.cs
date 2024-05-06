using Google.Protobuf;

namespace App.Network {
    public class SessionPacket {
        public uint Opcode { get; private set; }
        public IMessage Data { get; private set; }
        public IPacketHandler PacketHandler { get; private set; }

        public SessionPacket(uint opcode, IMessage data, IPacketHandler handler) {
            Opcode = opcode;
            Data = data;
            PacketHandler = handler;
        }
    }
}
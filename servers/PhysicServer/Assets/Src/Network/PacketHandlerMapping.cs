using System.Collections.Generic;

namespace App.Network {
    public class PacketHandlerMapping {
        private Dictionary<uint, IPacketHandler> _handlers = new();

        public void AddHandler<T>(uint opcode, PacketHandler<T> handler) {
            _handlers[opcode] = handler;
        }

        public IPacketHandler GetHandler(uint opcode) {
            return _handlers[opcode];
        }
    }
}
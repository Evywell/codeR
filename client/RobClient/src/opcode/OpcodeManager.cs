using System.Collections.Generic;
using Fr.Raven.Proto.Message.Gateway;

namespace RobClient.Opcode {
    public class OpcodeManager {
        private Dictionary<int, IOpcodeHandler> _handlers;

        public OpcodeManager() {
            _handlers = new Dictionary<int, IOpcodeHandler>();
        }

        public void AttachHandler(int opcode, IOpcodeHandler handler) {
            _handlers.Add(opcode, handler);
        }

        public void HandleOpcode(int opcode, Packet packet) {
            var handler = _handlers[opcode];

            if (handler == null) {
                return;
            }

            handler.Call(handler.GetParsedMessage(packet.Body), packet);
        }
    }
}

using System;
using System.Collections.Generic;
using Fr.Raven.Proto.Message.Gateway;
using UnityEngine;

namespace Core.Networking.Routing
{
    /// <summary>
    /// Routes incoming gateway packets to the appropriate handler based on context and opcode.
    /// Only processes packets with Context.Game.
    /// </summary>
    public class PacketRouter
    {
        private readonly Dictionary<int, IPacketHandler> _handlers = new Dictionary<int, IPacketHandler>();

        /// <summary>
        /// Registers a handler for a specific opcode.
        /// </summary>
        public void RegisterHandler(int opcode, IPacketHandler handler)
        {
            _handlers[opcode] = handler;
        }

        /// <summary>
        /// Routes a gateway packet to the registered handler.
        /// Only dispatches packets with Context.Game to handlers.
        /// Logs all other contexts for debugging.
        /// </summary>
        public void Route(Packet packet)
        {
            if (packet.Context != Packet.Types.Context.Game)
            {
                Debug.Log($"[PacketRouter] Received {packet.Context} packet (opcode=0x{packet.Opcode:X2}, {packet.Body.Length} bytes)");
                return;
            }

            if (_handlers.TryGetValue(packet.Opcode, out IPacketHandler handler))
            {
                try
                {
                    handler.Handle(packet.Body);
                }
                catch (Exception ex)
                {
                    Debug.LogError($"[PacketRouter] Error handling opcode 0x{packet.Opcode:X2}: {ex}");
                }
            }
            else
            {
                Debug.LogWarning($"[PacketRouter] No handler for Game opcode 0x{packet.Opcode:X2}");
            }
        }
    }
}

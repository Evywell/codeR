using System;
using Fr.Raven.Proto.Message.Gateway;

namespace Core.Networking.Gateway
{
    /// <summary>
    /// Receives decoded packets from the gateway. Implementations fire PacketReceived
    /// on the main thread after draining the internal queue.
    /// </summary>
    public interface IPacketReceiver
    {
        event Action<Packet> PacketReceived;

        /// <summary>
        /// Drains the internal packet queue and fires PacketReceived for each packet.
        /// Must be called from the main thread (e.g., in MonoBehaviour.Update).
        /// </summary>
        void ProcessIncomingPackets();
    }
}

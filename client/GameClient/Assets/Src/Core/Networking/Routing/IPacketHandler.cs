using Google.Protobuf;

namespace Core.Networking.Routing
{
    /// <summary>
    /// Handles a deserialized game packet body for a specific opcode.
    /// </summary>
    public interface IPacketHandler
    {
        void Handle(ByteString body);
    }
}

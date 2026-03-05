using Google.Protobuf;

namespace Core.Networking.Routing
{
    /// <summary>
    /// Handles a deserialized game packet body for a specific opcode.
    /// Each handler declares which opcode it handles via the Opcode property.
    /// </summary>
    public interface IPacketHandler
    {
        int Opcode { get; }
        void Handle(ByteString body);
    }
}

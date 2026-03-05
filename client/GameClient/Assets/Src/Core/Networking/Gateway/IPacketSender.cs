using System.Threading.Tasks;
using Fr.Raven.Proto.Message.Gateway;
using Google.Protobuf;

namespace Core.Networking.Gateway
{
    /// <summary>
    /// Sends protobuf messages to the gateway with a specified opcode and context.
    /// </summary>
    public interface IPacketSender
    {
        Task SendMessage(int opcode, IMessage body, Packet.Types.Context context);
    }
}

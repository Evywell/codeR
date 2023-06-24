using System.Threading.Tasks;
using Fr.Raven.Proto.Message.Gateway;
using Google.Protobuf;

namespace RobClient.Network {
    public interface IMessageSender {
        Task SendMessage(int opcode, IMessage message, Packet.Types.Context destination);
    } 
}
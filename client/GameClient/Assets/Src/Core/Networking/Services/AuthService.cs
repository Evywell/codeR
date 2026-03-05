using System.Threading.Tasks;
using Core.Networking.Gateway;
using Fr.Raven.Proto.Message.Eas;
using Fr.Raven.Proto.Message.Gateway;

namespace Core.Networking.Services
{
    /// <summary>
    /// Handles authentication with the gateway's EAS extension.
    /// </summary>
    public class AuthService
    {
        private readonly IPacketSender _sender;

        public AuthService(IPacketSender sender)
        {
            _sender = sender;
        }

        public async Task AuthenticateWithUserId(int userId)
        {
            var devAuthPacket = new DevAuthenticationPacket
            {
                UserId = (uint)userId
            };

            var authMessage = new EasPacket
            {
                AuthType = EasPacket.Types.Type.Dev,
                DevAuthPacket = devAuthPacket
            };

            await _sender.SendMessage(0, authMessage, Packet.Types.Context.Eas);
        }
    }
}

using Fr.Raven.Proto.Message.Eas;
using Fr.Raven.Proto.Message.Gateway;
using Google.Protobuf;

namespace RobClient.Eas;

public class EasContext {
    private Client _client;

    internal EasContext(Client client) {
        _client = client;
        _client.AddOnPackedReceivedListener(packet => {
            if (packet.Opcode == 0x01 && packet.Context == Packet.Types.Context.Eas) {
                var authenticationResult = EasAuthenticationResult.Parser.ParseFrom(packet.Body);

                this.WhenAuthenticated(authenticationResult.IsAuthenticatedSuccessfully);

                return true;
            }

            return false;
        });
    }

    public async Task Authenticate() {
        var devAuthPacket = new DevAuthenticationPacket();
        devAuthPacket.UserId = 1;
        
        var authPacket = new EasPacket();
        authPacket.AuthType = EasPacket.Types.Type.Dev;
        authPacket.DevAuthPacket = devAuthPacket;

        var packet = new Packet();
        packet.Context = Packet.Types.Context.Eas;
        packet.Body = authPacket.ToByteString();

        await _client.Send(packet);
    }

    private void WhenAuthenticated(bool authenticationResult) {
        if (authenticationResult) {
            Console.WriteLine("Authenticated successfully");
        } else {
            Console.Error.WriteLine("Authentication failed");
        }
    }
}

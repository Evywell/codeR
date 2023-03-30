using Fr.Raven.Proto.Message.Gateway;
using Fr.Raven.Proto.Message.Realm;
using Google.Protobuf;

namespace RobClient.Realm;

public class RealmContext {

    private Client _client;

    internal RealmContext(Client client) {
        _client = client;
        _client.AddOnPackedReceivedListener(packet => {
            if (packet.Opcode == 0x02 && packet.Context == Packet.Types.Context.Realm) {
                Console.WriteLine("Character reserved !");

                return true;
            }

            return false;
        });
    }

    public async Task ReserveCharacterWithId(int characterId) {

        var joinTheWorld = new JoinTheWorld();
        joinTheWorld.CharacterId = (uint)characterId;

        var packet = new Packet();
        packet.Opcode = 0x03;
        packet.Context = Packet.Types.Context.Realm;
        packet.Body = joinTheWorld.ToByteString();

        await _client.Send(packet);
    }
}
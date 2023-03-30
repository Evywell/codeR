using RobClient.Eas;
using RobClient.Game;
using RobClient.Game.View;
using RobClient.Network;
using RobClient.Realm;

namespace RobClient;

public class Entrypoint {

    private Client _client;
    private NettyClient? _process = null;
    public EasContext Eas
    { get; private set; }

    public RealmContext Realm
    { get; private set; }

    public GameContext Game
    { get; private set; }

    public Entrypoint(IObjectViewFactory objectViewFactory) {
        _client = new Client();
        Eas = new EasContext(_client);
        Realm = new RealmContext(_client);
        Game = new GameContext(_client, objectViewFactory);
    }

    public async Task ConnectToGateway() {
        _process = new NettyClient(_client);

        await _process.ConnectTo("127.0.0.1", 11111);
    }

    public async Task Disconnect() {
        if (_process == null) {
            return;
        }

        await _process.Close();
    }
}
using RobClient;
using RobClient.Game;

namespace Sandbox;

class Program {
    static async Task RunClientAsync() {
        Entrypoint client = new Entrypoint(new NullObjectViewFactory(), new ConsoleLogger());

        WatchGameEnvironment(client.Game.GameEnvironment);

        await client.ConnectToGateway();
        await client.Eas.Authenticate();

        await StartScenario(client);

        Console.ReadLine();
    }

    static void Main() => RunClientAsync().Wait();

    private static async Task StartScenario(Entrypoint client) {
        await client.Realm.ReserveCharacterWithId(1);
        await client.Game.JoinWorld();

        var playerInteraction = client.Game.PlayerInteraction;
        playerInteraction.Move(10);
    }

    private static void WatchGameEnvironment(GameEnvironment gameEnvironment) {
        GameClientWatcher watcher = new GameClientWatcher(gameEnvironment);

        Thread InstanceCaller = new Thread(new ThreadStart(watcher.PrintObjects));

        InstanceCaller.Start();
    }
}
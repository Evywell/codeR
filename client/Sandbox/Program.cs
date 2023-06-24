using RobClient;
using RobClient.Network;

namespace Sandbox;

class Program {
    static async Task RunClientAsync() {
        var communication = new GatewayCommunication("127.0.0.1", 11111);
        var gameClientFactory = new GameClientFactory();
        var gameClient = gameClientFactory.Create(communication, communication);

        gameClient.Game.WorldObjectUpdatedSub.Subscribe(Console.WriteLine);

        await gameClient.AuthenticateWithUserId(1);
        await gameClient.Realm.JoinWorldWithCharacter(1);
        gameClient.Interaction.Move(0);

        Console.ReadLine();
    }

    static void Main() => RunClientAsync().Wait();
}
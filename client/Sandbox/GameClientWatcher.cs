using RobClient.Game;

namespace Sandbox;

public class GameClientWatcher {
    private GameEnvironment _gameEnvironment;

    public GameClientWatcher(GameEnvironment gameEnvironment) {
        _gameEnvironment = gameEnvironment;
    }

    public void PrintObjects() {
        Console.WriteLine("=======");

        foreach (var entry in _gameEnvironment.GetWorldObjects()) {
            Console.WriteLine($"{entry.Key}: {entry.Value}");
        }

        Console.WriteLine("=======");

        Thread.Sleep(5000);

        PrintObjects();
    }
}
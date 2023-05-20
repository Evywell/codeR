using RobClient;

namespace Sandbox;

public class ConsoleLogger : ILogger
{
    public void Debug(string message)
    {
        Console.WriteLine(message);
    }
}
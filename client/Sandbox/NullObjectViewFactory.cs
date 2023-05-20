using RobClient.Game.Entity;
using RobClient.Game.View;

namespace Sandbox;

public class NullObjectViewFactory : IObjectViewFactory
{
    public void Create(WorldObject worldObject)
    {
        // Doing nothing
        Console.WriteLine($"Constructing {worldObject.Guid.GetRawValue()}");
    }
}
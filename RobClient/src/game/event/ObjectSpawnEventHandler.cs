using RobClient.Event;
using RobClient.Game.Entity;
using RobClient.Game.Entity.Guid;
using RobClient.Game.View;

namespace RobClient.Game.Event;

public class ObjectSpawnEventHandler : IEventHandler
{
    private ObjectFactory _objectFactory;
    private GameEnvironment _gameEnvironment;

    private ViewEnvironment _viewEnvironment;

    public ObjectSpawnEventHandler(
        ObjectFactory objectFactory,
        GameEnvironment gameEnvironment,
        ViewEnvironment viewEnvironment
    ) {
        _objectFactory = objectFactory;
        _gameEnvironment = gameEnvironment;
        _viewEnvironment = viewEnvironment;
    }

    public void Call(ObjectSpawnEvent sourceEvent)
    {
        var gameObject = _objectFactory.Create(
            ObjectGuid.From(sourceEvent.Guid),
            new Vector4f(
                sourceEvent.Position.X,
                sourceEvent.Position.Y,
                sourceEvent.Position.Z,
                sourceEvent.Orientation
            )
        );

        _gameEnvironment.AttachObject(gameObject);
        _viewEnvironment.AttachObject(gameObject);

        Console.WriteLine(sourceEvent.ToString());
    }

    public void Call(IEvent sourceEvent) => Call((ObjectSpawnEvent)sourceEvent);

    public Type getEventType() => typeof(ObjectSpawnEvent);
}
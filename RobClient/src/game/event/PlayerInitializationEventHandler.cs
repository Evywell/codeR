using RobClient.Event;
using RobClient.Game.Entity;
using RobClient.Game.Entity.Guid;

namespace RobClient.Game.Event;

public class PlayerInitializationEventHandler : IEventHandler
{
    private ObjectFactory _objectFactory;

    private GameEnvironment _gameEnvironment;

    public PlayerInitializationEventHandler(
        ObjectFactory objectFactory,
        GameEnvironment gameEnvironment
    ) {
        _objectFactory = objectFactory;
        _gameEnvironment = gameEnvironment;
    }

    public void Call(PlayerInitializationEvent sourceEvent) {
        var player = _objectFactory.CreatePlayer(
            sourceEvent.Guid,
            sourceEvent.Name,
            sourceEvent.Position
        );

        _gameEnvironment.AttachObject(player);
    }

    public void Call(IEvent sourceEvent) => Call((PlayerInitializationEvent)sourceEvent);

    public Type getEventType() => typeof(PlayerInitializationEvent);
}
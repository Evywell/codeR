using RobClient.Event;
using RobClient.Game.Event;

namespace RobClient.Tests.Watcher;

public class WatchObjectMovingHandler : BaseEventHandler
{
    private ObjectMovedWatcher _watcher;

    public WatchObjectMovingHandler(ObjectMovedWatcher watcher) {
        _watcher = watcher;
    }

    public override void Call(IEvent sourceEvent)
    {
        _watcher.NotifyObjectMoved((ObjectMovedEvent) sourceEvent);
    }

    public override Type getEventType() => typeof(ObjectMovedEvent);
}
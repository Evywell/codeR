using RobClient.Event;

namespace RobClient.Tests.Watcher;

public class WatchEventHandler<T> : BaseEventHandler where T : IEvent
{
    private EventWatcher<T> _watcher;

    public WatchEventHandler(EventWatcher<T> watcher) {
        _watcher = watcher;
    }

    public override void Call(IEvent sourceEvent)
    {
        _watcher.NotifyObjectMoved((T) sourceEvent);
    }

    public override Type getEventType() => typeof(T);
}
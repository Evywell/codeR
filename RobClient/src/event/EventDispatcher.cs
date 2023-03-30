namespace RobClient.Event;

public class EventDispatcher {
    private IList<IEventHandler> _handlers = new List<IEventHandler>();

    public void AddEventHandler(IEventHandler handler) {
        _handlers.Add(handler);
    }

    public void Dispatch(IEvent sourceEvent) {
        foreach (var handler in _handlers) {
            if (handler.Support(sourceEvent)) {
                handler.Call(sourceEvent);
            }
        }
    }
}
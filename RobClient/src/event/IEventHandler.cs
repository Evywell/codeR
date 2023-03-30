namespace RobClient.Event;

public interface IEventHandler {
    bool Support(IEvent sourceEvent) {

        return sourceEvent.GetType() == this.getEventType();
    }

    void Call(IEvent sourceEvent);
    Type getEventType();
}
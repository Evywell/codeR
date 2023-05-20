using System;

namespace RobClient.Event {
    public interface IEventHandler {
        bool Support(IEvent sourceEvent);

        void Call(IEvent sourceEvent);
        Type getEventType();
    }
}

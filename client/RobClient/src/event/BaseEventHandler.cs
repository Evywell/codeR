using System;

namespace RobClient.Event {
    public abstract class BaseEventHandler : IEventHandler
    {
        public bool Support(IEvent sourceEvent) {

            return sourceEvent.GetType() == this.getEventType();
        }

        public abstract void Call(IEvent sourceEvent);

        public abstract Type getEventType();
    }
}
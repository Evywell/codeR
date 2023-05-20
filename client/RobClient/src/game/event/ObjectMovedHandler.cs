using System;
using RobClient.Event;
using RobClient.Game.Entity;
using RobClient.Game.Entity.Guid;

namespace RobClient.Game.Event {
    public class ObjectMovedEventHandler : BaseEventHandler
    {
        private GameEnvironment _gameEnvironment;

        public ObjectMovedEventHandler(GameEnvironment gameEnvironment) {
            _gameEnvironment = gameEnvironment;
        }

        public void Call(ObjectMovedEvent sourceEvent)
        {
            _gameEnvironment.UpdateObjectPosition(
                ObjectGuid.From(sourceEvent.Guid), 
                new Vector4f(sourceEvent.Position.X, sourceEvent.Position.Y, sourceEvent.Position.Z, sourceEvent.Orientation)
            );
        }

        public override void Call(IEvent sourceEvent) => Call((ObjectMovedEvent)sourceEvent);

        public override Type getEventType() => typeof(ObjectMovedEvent);
    }
}
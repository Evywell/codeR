using System;
using RobClient.Event;
using RobClient.Game.Entity;

namespace RobClient.Game.Event {
    public class PlayerInitializationEventHandler : BaseEventHandler
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
            _gameEnvironment.ControlledObjectId = player.Guid;
        }

        public override void Call(IEvent sourceEvent) => Call((PlayerInitializationEvent)sourceEvent);

        public override Type getEventType() => typeof(PlayerInitializationEvent);
    }
}

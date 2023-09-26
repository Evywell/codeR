using System;
using RobClient.Framework;
using RobClient.Game.Entity;

namespace RobClient.Game.Interaction.Movement {
    public class MovementAction : IAction
    {
        private StateProxy<WorldObject> _sourceProxy;
        private MovementInfo _movementInfo;

        public MovementAction(StateProxy<WorldObject> sourceProxy, MovementInfo movementInfo)
        {
            _sourceProxy = sourceProxy;
            _movementInfo = movementInfo;
        }

        public void Invoke(int deltaTime)
        {
            var worldObject = _sourceProxy.GetSource();
            var orientationDeg = _movementInfo.HorizontalOrientation;
            var orientationRadian = orientationDeg * 0.017453f;
            var traveledDistance = worldObject.Speed * (deltaTime / 1000f);
            var distanceXAxis = Math.Cos(orientationRadian) * traveledDistance;
            var distanceYAxis = Math.Sin(orientationRadian) * traveledDistance;

            _sourceProxy.SetSourceValue(() => {
                worldObject.Position.X += (float)Math.Round(distanceXAxis, 3, MidpointRounding.AwayFromZero);
                worldObject.Position.Y += (float)Math.Round(distanceYAxis, 3, MidpointRounding.AwayFromZero);
                worldObject.Position.O = orientationDeg;
            });
        }

        public bool ShouldBeRepeated()
        {
            return true;
        }
    }
}
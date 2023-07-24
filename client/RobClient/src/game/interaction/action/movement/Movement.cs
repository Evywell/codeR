using System;
using RobClient.Framework;
using RobClient.Game.Entity;

namespace RobClient.Game.Interaction.Action.Movement {
    public class Movement : IAction
    {
        private StateProxy<WorldObject> _sourceProxy;
        private float _orientation;

        public Movement(StateProxy<WorldObject> sourceProxy, float orientation)
        {
            _sourceProxy = sourceProxy;
            _orientation = orientation;
        }

        public void Invoke(int deltaTime)
        {
            var worldObject = _sourceProxy.GetSource();
            var orientationRadian = _orientation * 0.017453f;
            var traveledDistance = worldObject.Speed * (deltaTime / 1000f);
            var distanceXAxis = Math.Cos(orientationRadian) * traveledDistance;
            var distanceYAxis = Math.Sin(orientationRadian) * traveledDistance;

            _sourceProxy.SetSourceValue(() => {
                worldObject.Position.X += (float)Math.Round(distanceXAxis, 3, MidpointRounding.AwayFromZero);
                worldObject.Position.Y += (float)Math.Round(distanceYAxis, 3, MidpointRounding.AwayFromZero);
                worldObject.Position.O = _orientation;
            });
        }

        public bool ShouldBeRepeated()
        {
            return true;
        }
    }
}
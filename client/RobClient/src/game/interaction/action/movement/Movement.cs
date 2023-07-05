using System;
using RobClient.Game.Entity;

namespace RobClient.Game.Interaction.Action.Movement {
    public class Movement : IAction
    {
        private WorldObject _source;
        private float _orientation;

        public Movement(WorldObject source, float orientation)
        {
            _source = source;
            _orientation = orientation;
        }

        public void Invoke(int deltaTime)
        {
            var orientationRadian = _orientation * 0.017453f;
            var traveledDistance = _source.Speed * (deltaTime / 1000f);
            var distanceXAxis = Math.Cos(orientationRadian) * traveledDistance;
            var distanceYAxis = Math.Sin(orientationRadian) * traveledDistance;

            _source.Position.X += (float)Math.Round(distanceXAxis, 3, MidpointRounding.AwayFromZero);
            _source.Position.Y += (float)Math.Round(distanceYAxis, 3, MidpointRounding.AwayFromZero);
            _source.Position.O = _orientation;
        }

        public bool ShouldBeRepeated()
        {
            return true;
        }
    }
}
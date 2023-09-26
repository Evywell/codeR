using System;

namespace RobClient.Game.Interaction.Movement {
    public class MovementInfo
    {
        public bool IsMoving
        { get; private set; }
        
        public MovementDirection Direction
        { get; private set; }
        
        public float HorizontalOrientation
        { get; private set; }
        
        public float VerticalOrientation
        { get; private set; }
        
        private MovementModificator _modificators;
        
        public MovementInfo(
            MovementDirection direction,
            MovementModificator modificators,
            float horizontalOrientation,
            float verticalOrientation
        ) {
            ValidateMovementModificators(modificators);
            
            if (verticalOrientation != 0 && !CanMoveVertically())
            {
                throw new ArgumentException();
            }
            
            _modificators = modificators;
            
            IsMoving = !modificators.HasFlag(MovementModificator.NONE);
            Direction = direction;
            HorizontalOrientation = horizontalOrientation;
            VerticalOrientation = verticalOrientation;
        }
        
        private bool CanMoveVertically()
        {
            return _modificators.HasFlag(MovementModificator.SWIM) || _modificators.HasFlag(MovementModificator.FLY);
        }
        
        private void ValidateMovementModificators(MovementModificator modificators)
        {
            if (modificators.HasFlag(MovementModificator.WALK) && modificators.HasFlag(MovementModificator.SPRINT))
            {
                throw new ArgumentException();
            }
            
            if (modificators.HasFlag(MovementModificator.JUMP) && modificators.HasFlag(MovementModificator.SWIM))
            {
                throw new ArgumentException();
            }
        }
    }

    public enum MovementDirection
    {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        IDLE,
    }

    [Flags]
    public enum MovementModificator : ushort
    {
        NONE   = 0b00000,
        WALK   = 0b00001,
        SPRINT = 0b00010,
        JUMP   = 0b00100,
        SWIM   = 0b01000,
        FLY    = 0b10000
    }
}
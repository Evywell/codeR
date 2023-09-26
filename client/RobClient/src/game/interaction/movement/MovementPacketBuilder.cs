using System;
using System.Diagnostics;
using Fr.Raven.Proto.Message.Game;
using RobClient.Game.Interaction.Movement;

namespace RobClient.game.interaction.movement
{
    public class MovementPacketBuilder {
        public static ProceedMovement CreateFromMovementInfo(MovementInfo movementInfo)
        {
            return new ProceedMovement()
            {
                Phase = MovementPhase.PhaseBegin,
                Direction = GetFromInfo(movementInfo),
                Orientation = movementInfo.HorizontalOrientation
            };
        }
        
        private static MovementDirectionType GetFromInfo(MovementInfo movementInfo)
        {
            if (movementInfo.Direction == MovementDirection.FORWARD)
            {
                return MovementDirectionType.TypeForward;
            }
            
            throw new NotImplementedException();
        }
    }
}

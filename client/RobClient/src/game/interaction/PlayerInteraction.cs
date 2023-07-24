using Fr.Raven.Proto.Message.Game;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using RobClient.Game.Interaction.Action.Movement;
using RobClient.Network;

namespace RobClient.Game.Interaction {
    public class PlayerInteraction {
        private GameEnvironment _gameEnvironment;
        private IMessageSender _sender;

        public PlayerInteraction(
            GameEnvironment gameEnvironment,
            IMessageSender sender
        ) {
            _gameEnvironment = gameEnvironment;
            _sender = sender;
        }

        public void Move(float orientation) {
            _gameEnvironment.AddAction(
                new Movement(_gameEnvironment.UseStateProxyFor(_gameEnvironment.GetControlledObject()), orientation)
            );

            var movement = new ProceedMovement();
            movement.Phase = MovementPhase.PhaseBegin;
            movement.Direction = MovementDirectionType.TypeForward;
            movement.Orientation = orientation;
            
            _sender.SendMessage(0x06, movement, GatewayPacket.Types.Context.Game);
        }
    }
}
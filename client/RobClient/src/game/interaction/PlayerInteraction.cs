using RobClient.game.interaction.movement;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using RobClient.Game.Interaction.Movement;
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

        public void Move(MovementInfo movementInfo) {
            _gameEnvironment.AddAction(
                new MovementAction(
                    _gameEnvironment.UseStateProxyFor(_gameEnvironment.GetControlledObject()),
                    movementInfo
                )
            );
            
            _sender.SendMessage(
                0x06,
                MovementPacketBuilder.CreateFromMovementInfo(movementInfo),
                GatewayPacket.Types.Context.Game
            );
        }
    }
}
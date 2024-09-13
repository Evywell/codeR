using Fr.Raven.Proto.Message.Game;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using RobClient.Game.Interaction.Action.Movement;
using RobClient.Network;
using RobClient.Game.Entity.Guid;
using RobClient.Game.Entity;

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

        public void MoveClient(float posX, float posY, float posZ, float orientation, Vector3f direction) {
            var position = new Position
            {
                PosX = posX,
                PosY = posY,
                PosZ = posZ,
                Orientation = orientation
            };

            var movement = new SMovementInfo
            {
                Phase = MovementPhase.PhaseBegin,
                Position = position,
                Direction = new Direction {
                    X = direction.X,
                    Y = direction.Y,
                    Z = direction.Z,
                }
            };

            _sender.SendMessage(0x06, movement, GatewayPacket.Types.Context.Game);
        }

        public void CastSpell(uint spellId) {
            var message = new CastSpell
            {
                SpellId = spellId
            };

            _sender.SendMessage(0x07, message, GatewayPacket.Types.Context.Game);
        }

        public void EngageCombat(ObjectGuid target) {
            var message = new PlayerEngageCombat
            {
                Target = target.GetRawValue()
            };

            _sender.SendMessage(0x09, message, GatewayPacket.Types.Context.Game);
        }
    }
}
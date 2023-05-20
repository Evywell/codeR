using Fr.Raven.Proto.Message.Game;
using GatewayPacket = Fr.Raven.Proto.Message.Gateway.Packet;
using RobClient.Game.Entity;
using RobClient.Game.Interaction.Action.Movement;
using RobClient.Game.World;
using Google.Protobuf;

namespace RobClient.Game.Interaction {
    public class PlayerInteraction {
        private Client _client;
        private LocalWorld _world;

        internal PlayerInteraction(Client client, LocalWorld world) {
            _client = client;
            _world = world;
        }

        public void Move(float orientation) {
            _world.AddAction(new Movement());

            var payload = new ProceedMovement();
            payload.Phase = MovementPhase.PhaseBegin;
            payload.Direction = MovementDirectionType.TypeForward;
            payload.Orientation = orientation;

            var packet = new GatewayPacket();
            packet.Opcode = 0x06;
            packet.Body = payload.ToByteString();
            packet.Context = GatewayPacket.Types.Context.Game;
            
            _ =_client.Send(packet);
        }
    }
}
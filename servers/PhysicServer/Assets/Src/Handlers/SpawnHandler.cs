using App.Network;
using Fr.Raven.Proto.Message.Physicbridge;
using Google.Protobuf;

namespace App.Handlers {
    public class SpawnHandler : PacketHandler<SpawnRequest>
    {
        private ObjectSpawner _spawner;

        public SpawnHandler(ObjectSpawner spawner) {
            _spawner = spawner;
        }

        public override IMessage CreateMessageFromPayload(ByteString payload) => SpawnRequest.Parser.ParseFrom(payload);

        public override void HandleOpcode(uint opcode, SpawnRequest message)
        {
            _spawner.Spawn(message.Guid, message.Position);
        }
    }
}
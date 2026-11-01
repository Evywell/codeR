using App.Managers;
using App.Network;
using Fr.Raven.Proto.Message.Physicbridge;
using Google.Protobuf;
using App.Normalizers;

namespace App.Handlers {
    public class MoveHandler : PacketHandler<MoveRequest>
    {
        private ActorsManager _actorsManager;
        private ObjectSpawner _spawner;

        public MoveHandler(ActorsManager actorsManager, ObjectSpawner spawner) {
            _actorsManager = actorsManager;
            _spawner = spawner;
        }

        public override IMessage CreateMessageFromPayload(ByteString payload) => MoveRequest.Parser.ParseFrom(payload);

        public override void HandleOpcode(uint opcode, MoveRequest message)
        {
            var actor = _actorsManager.GetEntityByGuid(message.Guid) ?? _spawner.Spawn(message.Guid, message.Origin);

            var agent = actor.GetComponent<ObjectController>();

            agent.MoveToPosition(PositionNormalizer.TransformServerPositionToUnityPosition(message.Destination));
        }
    }
}

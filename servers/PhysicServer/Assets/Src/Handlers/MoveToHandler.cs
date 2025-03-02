using App.Managers;
using App.Network;
using Fr.Raven.Proto.Message.Physicbridge;
using Google.Protobuf;
using App.Normalizers;

namespace App.Handlers {
    public class MoveToHandler : PacketHandler<MoveToRequest>
    {
        private ActorsManager _actorsManager;

        public MoveToHandler(ActorsManager actorsManager) {
            _actorsManager = actorsManager;
        }

        public override IMessage CreateMessageFromPayload(ByteString payload) => MoveToRequest.Parser.ParseFrom(payload);

        public override void HandleOpcode(uint opcode, MoveToRequest message)
        {
            var actor = _actorsManager.GetEntityByGuid(message.Guid);

            if (actor == null) {
                return;
            }

            var position = message.Position;

            var agent = actor.GetComponent<ObjectController>();

            agent.MoveToPosition(PositionNormalizer.TransformServerPositionToUnityPosition(position));
        }
    }
}
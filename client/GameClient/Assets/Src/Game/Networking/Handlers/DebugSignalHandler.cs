using Core.Networking.Protocol;
using Core.Networking.Routing;
using Fr.Raven.Proto.Message.Game;
using Game.State;
using Google.Protobuf;

namespace Game.Networking.Handlers
{
    /// <summary>
    /// Handles SMSG_DEBUG_SIGNAL (0x97).
    /// Forwards debug signals to the world state for consumption by debug tools.
    /// </summary>
    public class DebugSignalHandler : IPacketHandler
    {
        private readonly WorldState _worldState;

        public int Opcode => Opcodes.SMSG_DEBUG_SIGNAL;

        public DebugSignalHandler(WorldState worldState)
        {
            _worldState = worldState;
        }

        public void Handle(ByteString body)
        {
            DebugSignal signal = DebugSignal.Parser.ParseFrom(body);
            _worldState.DispatchDebugSignal(signal.Name, signal.Value);
        }
    }
}

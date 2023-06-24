using System;
using System.Reactive.Linq;
using RobClient.Network;
using Packet = Fr.Raven.Proto.Message.Gateway.Packet;

namespace RobClient.Game.World {
    public class WorldApi {
        public IObservable<Packet> GamePacketReceivedObs
        { get; private set; }

        private IMessageSender _sender;

        public WorldApi(IMessageSender sender, IPacketReceiver receiver) {
            _sender = sender;

            GamePacketReceivedObs = receiver
                .GetPacketReceivedObservable()
                .Where(packet => packet.Context == Packet.Types.Context.Game);

            GamePacketReceivedObs.Subscribe(packet => {
                Console.WriteLine($"Packet received with opcode {packet.Opcode}");
            });
        }
    }
}
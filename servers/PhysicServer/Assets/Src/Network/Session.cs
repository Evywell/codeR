using System.Collections.Concurrent;
using DotNetty.Transport.Channels;
using Google.Protobuf;

namespace App.Network {
    public class Session {
        public int Identifier
        { get; private set; }

        public ConcurrentQueue<SessionPacket> PacketQueue
        { get; private set; }

        private IChannel _channel;

        public Session(int identifier, IChannel channel) {
            Identifier = identifier;
            PacketQueue = new ConcurrentQueue<SessionPacket>();

            _channel = channel;
        }

        public void Send(IMessage message) {
            _channel.WriteAndFlushAsync(message);
        }
    }
}
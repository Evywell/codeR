using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using DotNetty.Transport.Channels;
using Fr.Raven.Proto.Message.Gateway;
using Version = Fr.Raven.Proto.Message.Gateway.Version;

namespace RobClient {
    class Client {
        private IChannel _channel;
        private ILogger _logger;
        private List<Listener> _onPacketReceivedListeners = new List<Listener>();

        public Client(ILogger logger = null) {
            _logger = logger;
        }

        public void OnPacketReceived(Packet packet) {
            _logger?.Debug($"Packet received from {packet.Context} with opcode {packet.Opcode}");

            foreach (var listener in _onPacketReceivedListeners) {
                listener.Call(packet);
            }
        }
        
        public void OnConnectionEstablished(IChannel channel) {
            _channel = channel;
        }

        public async Task Send(Packet packet) {
            if (_channel == null) {
                throw new Exception("Cannot send packet when connection is not etablished yet");
            }

            var version = new Version();
            version.MajorVersion = 1;
            version.MinorVersion = 1;
            version.PatchVersion = 0;

            packet.Version = version;

            packet.CreatedAt = 1;

            await _channel.WriteAndFlushAsync(packet);
        }

        public void AddOnPackedReceivedListener(Func<Packet, Boolean> callback) {
            _onPacketReceivedListeners.Add(new Listener(callback));
        }
    }
}


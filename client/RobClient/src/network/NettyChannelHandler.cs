using System;
using DotNetty.Common.Utilities;
using DotNetty.Transport.Channels;
using Fr.Raven.Proto.Message.Gateway;

namespace RobClient.Network {
    class NettyChannelHandler : ChannelHandlerAdapter {
        private Client _client;
        
        public NettyChannelHandler(Client client) {
            _client = client;
        }
        
        public override void ChannelRead(IChannelHandlerContext context, object message) {
            _client.OnPacketReceived((Packet)message);

            ReferenceCountUtil.Release(message);
        }

        public override void ChannelActive(IChannelHandlerContext context) {
            _client.OnConnectionEstablished(context.Channel);
        }

        public override void ExceptionCaught(IChannelHandlerContext context, Exception exception) {
            Console.WriteLine(exception.ToString());
        }
    }
}

using DotNetty.Codecs;
using DotNetty.Codecs.Protobuf;
using DotNetty.Transport.Channels;
using DotNetty.Transport.Channels.Sockets;
using Fr.Raven.Proto.Message.Gateway;

namespace RobClient.Network {
    class ProtobufNettyChannelInitializer : ChannelInitializer<ISocketChannel>
    {
        private Client _client;

        public ProtobufNettyChannelInitializer(Client client) {
            _client = client;
        }

        protected override void InitChannel(ISocketChannel channel)
        {
            IChannelPipeline pipeline = channel.Pipeline;
            pipeline.AddLast("frameEncoder", new LengthFieldPrepender(4));
            pipeline.AddLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));

            pipeline.AddLast("protobufEncoder", new ProtobufEncoder());
            pipeline.AddLast("protobufDecoder", new ProtobufDecoder(Packet.Parser));

            pipeline.AddLast(new NettyChannelHandler(_client));
        }
    }
}

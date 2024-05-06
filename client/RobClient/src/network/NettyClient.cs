using System;
using System.Net;
using System.Threading.Tasks;
using DotNetty.Transport.Bootstrapping;
using DotNetty.Transport.Channels;
using DotNetty.Transport.Channels.Sockets;

namespace RobClient.Network {
    class NettyClient {
        private IEventLoopGroup group;
        private Bootstrap bootstrap;
        private IChannel _channel;
        private Client client;

        public NettyClient(Client _client) {
            this.group = new MultithreadEventLoopGroup();
            this.bootstrap = new Bootstrap();
            this.client = _client;

            bootstrap
                .Group(group)
                .Channel<TcpSocketChannel>()
                .Option(ChannelOption.TcpNodelay, true)
                //.Option(ChannelOption.SoKeepalive, true)
                .Handler(new ProtobufNettyChannelInitializer(this.client));
        }

        public async Task ConnectTo(string host, int port) {
            _channel = await this.bootstrap.ConnectAsync(
                new IPEndPoint(IPAddress.Parse(host), port)
            );
        }

        public async Task Close() {
            if(_channel == null) {
                throw new Exception("You must connect to a host before closing the connection");
            } 

            try {
                await _channel.CloseAsync();
            } finally {
                await this.ShutdownGracefully();
            }
        }

        private async Task ShutdownGracefully() {
            await this.group.ShutdownGracefullyAsync(TimeSpan.FromMilliseconds(100), TimeSpan.FromSeconds(1));
        }
    }
}

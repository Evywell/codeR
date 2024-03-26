using UnityEngine;
using DotNetty.Codecs;
using DotNetty.Transport.Bootstrapping;
using DotNetty.Transport.Channels;
using DotNetty.Transport.Channels.Sockets;
using DotNetty.Codecs.Protobuf;
using Fr.Raven.Proto.Message.Game;
using System.Collections.Concurrent;

public class PhysicServer : MonoBehaviour
{
    public ObjectSpawner spawner;

    private ConcurrentQueue<Position> _spawnQueue = new ConcurrentQueue<Position>();

    private async void Start()
    {
        var bossGroup = new MultithreadEventLoopGroup(1);
        var workerGroup = new MultithreadEventLoopGroup();

        var bootstrap = new ServerBootstrap();

        bootstrap
            .Group(bossGroup, workerGroup)
            .Channel<TcpServerSocketChannel>()
            .Option(ChannelOption.SoBacklog, 100) // maximum queue length for incoming connection
            .ChildHandler(new ActionChannelInitializer<ISocketChannel>(channel =>
            {
                IChannelPipeline pipeline = channel.Pipeline;

                // Decoders
                pipeline.AddLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
                pipeline.AddLast("protobufDecoder", new ProtobufDecoder(Packet.Parser));

                // Encoder
                pipeline.AddLast("frameEncoder", new LengthFieldPrepender(4));
                pipeline.AddLast("protobufEncoder", new ProtobufEncoder());

                pipeline.AddLast(new HelloWorldServerHandler(this));
            }));
                
        IChannel bootstrapChannel = await bootstrap.BindAsync(1111);
    }

    private void Update()
    {
        if (_spawnQueue.IsEmpty) {
            return;
        }

        while (_spawnQueue.TryDequeue(out Position position)) {
            spawner.Spawn(position);
        }
    }

    internal void AddSpawnInQueue(Position position)
    {
        _spawnQueue.Enqueue(position);
    }
}

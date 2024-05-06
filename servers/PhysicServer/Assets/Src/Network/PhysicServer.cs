using UnityEngine;
using DotNetty.Codecs;
using DotNetty.Transport.Bootstrapping;
using DotNetty.Transport.Channels;
using DotNetty.Transport.Channels.Sockets;
using DotNetty.Codecs.Protobuf;
using System.Collections.Generic;
using App.Handlers;
using App.Managers;
using Google.Protobuf;
using Fr.Raven.Proto.Message.Physicbridge;

namespace App.Network {
    public class PhysicServer : MonoBehaviour, ISessionHolder
    {
        public static PhysicServer Instance
        { get; private set; }
        public ObjectSpawner spawner;
        public ActorsManager actorsManager;

        private Dictionary<int, Session> _sessions = new Dictionary<int, Session>();

        public void SendMessageToEveryone(IMessage message) {
            foreach (var session in _sessions.Values) {
                session.Send(message);
            }
        }

        private void Awake() {
            if (Instance != null && Instance != this) {
                Destroy(this);
                return;
            }

            Instance = this;
        }

        private async void Start()
        {
            var packetHandlerMapping = new PacketHandlerMapping();
            packetHandlerMapping.AddHandler(1, new SpawnHandler(spawner));
            packetHandlerMapping.AddHandler(2, new MoveToHandler(actorsManager));

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

                    pipeline.AddLast(new PhysicServerHandler(packetHandlerMapping, this));
                }));
                    
            IChannel bootstrapChannel = await bootstrap.BindAsync(1111);
        }

        private void Update()
        {
            foreach (var session in _sessions.Values)
            {
                HandlePacketsOfSession(session);
            }
        }

        private void HandlePacketsOfSession(Session session)
        {
            if (session.PacketQueue.IsEmpty) {
                return;
            }

            var queueLength = session.PacketQueue.Count;
            var currentPacketIndex = 0;

            while (currentPacketIndex < queueLength && session.PacketQueue.TryDequeue(out SessionPacket packet)) {
                currentPacketIndex++;

                packet.PacketHandler.HandleOpcode(packet.Opcode, packet.Data);
            }
        }

        public void AddSession(Session session)
        {
            _sessions.Add(session.Identifier, session);
        }

        public IEnumerable<Session> GetSessionEnumerable()
        {
            return _sessions.Values;
        }

        public void RemoveSession(Session session)
        {
            RemoveSession(session.Identifier);
        }

        public Session GetSession(int identifier)
        {
            return _sessions[identifier];
        }

        public void RemoveSession(int identifier)
        {
            _sessions.Remove(identifier);
        }
    }
}

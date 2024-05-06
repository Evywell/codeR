using DotNetty.Transport.Channels;
using Fr.Raven.Proto.Message.Physicbridge;
using System;
using UnityEngine;

namespace App.Network {
    public class PhysicServerHandler : SimpleChannelInboundHandler<Packet>
    {
        private PacketHandlerMapping _packetHandlerMapping;
        private ISessionHolder _sessionHolder;

        public PhysicServerHandler(PacketHandlerMapping packetHandlerMapping, ISessionHolder sessionHolder) {
            _packetHandlerMapping = packetHandlerMapping;
            _sessionHolder = sessionHolder;
        }

        public override void ChannelActive(IChannelHandlerContext context)
        {
            Debug.Log($"Connection opened to {context.Channel.RemoteAddress}");

            _sessionHolder.AddSession(new Session(context.Channel.GetHashCode(), context.Channel));
        }

        // Catches inbound message exceptions
        public override void ExceptionCaught(IChannelHandlerContext context, Exception e)
        {
            Debug.LogError(e);
            context.CloseAsync();
            _sessionHolder.RemoveSession(context.Channel.GetHashCode());
        }

        protected override void ChannelRead0(IChannelHandlerContext context, Packet msg)
        {
            var opcode = msg.Opcode;
            var session = _sessionHolder.GetSession(context.Channel.GetHashCode());

            Debug.Log($"Message received with opcode {opcode}");

            var handler = _packetHandlerMapping.GetHandler(opcode);
            var message = handler.CreateMessageFromPayload(msg.Body);

            session.PacketQueue.Enqueue(new SessionPacket(opcode, message, handler));
        }

        public override bool IsSharable => true;
    }
}
using DotNetty.Transport.Channels;
using Fr.Raven.Proto.Message.Game;
using System;
using System.Net;
using UnityEngine;

public class HelloWorldServerHandler : SimpleChannelInboundHandler<Packet>
{
    private PhysicServer _server;

    public HelloWorldServerHandler(PhysicServer server) {
        _server = server;
    }

    public override void ChannelActive(IChannelHandlerContext context)
    {
        Debug.Log("Connection opened");
        context.WriteAndFlushAsync($"Welcome to {Dns.GetHostName()} !");
    }

    // Catches inbound message exceptions
    public override void ExceptionCaught(IChannelHandlerContext context, Exception e)
    {
        context.CloseAsync();
    }

    protected override void ChannelRead0(IChannelHandlerContext ctx, Packet msg)
    {
        var opcode = msg.Opcode;
        Debug.Log($"Message received with opcode {opcode}");

        if (opcode == 1) {
            var position = Position.Parser.ParseFrom(msg.Body);

            Debug.Log($"ça doit spawner ici {position}");
            _server.AddSpawnInQueue(position);
        }
    }

    public override bool IsSharable => true;
}
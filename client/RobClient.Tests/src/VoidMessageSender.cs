using Fr.Raven.Proto.Message.Gateway;
using Google.Protobuf;
using RobClient.Network;

namespace RobClient.Tests;

public class VoidMessageSender : IMessageSender
{
    public Task SendMessage(int opcode, IMessage message, Packet.Types.Context destination)
    {
        return Task.CompletedTask;
    }
}
using System;
using Fr.Raven.Proto.Message.Gateway;

namespace RobClient.Network {
    public interface IPacketReceiver {
        IObservable<Packet> GetPacketReceivedObservable();
    }
}
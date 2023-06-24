using System;
using System.Reactive.Subjects;
using Fr.Raven.Proto.Message.Gateway;

namespace RobClient.Network {
    public class SimplePacketReceiver : IPacketReceiver
    {
        private Subject<Packet> _messageReceivedSubj = new Subject<Packet>();

        public IObservable<Packet> GetPacketReceivedObservable()
        {
            return _messageReceivedSubj;
        }

        public void OnNext(Packet packet)
        {
            _messageReceivedSubj.OnNext(packet);
        }
    }
}
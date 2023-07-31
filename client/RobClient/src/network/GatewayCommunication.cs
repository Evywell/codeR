using System;
using System.Reactive.Subjects;
using System.Threading.Tasks;
using Fr.Raven.Proto.Message.Gateway;
using Google.Protobuf;

namespace RobClient.Network {
    public class GatewayCommunication : IMessageSender, IPacketReceiver
    {
        private string _gatewayHost;
        private int _gatewayPort;
        private Client _client;
        private NettyClient _gateway;
        private SimplePacketReceiver _packetReceiver = new SimplePacketReceiver();
        private Subject<Packet> _messageReceivedSubj = new Subject<Packet>();
        private bool isConnectedToGateway = false;

        public GatewayCommunication(string gatewayHost, int gatewayPort)
        {
            _gatewayHost = gatewayHost;
            _gatewayPort = gatewayPort;
            _client = new Client();
            _gateway = new NettyClient(_client);

            _client.AddOnPackedReceivedListener(packet => {
                _packetReceiver.OnNext(packet);

                return true;
            });
        }

        public IObservable<Packet> GetPacketReceivedObservable()
        {
            return _packetReceiver.GetPacketReceivedObservable();
        }

        public async Task SendMessage(int opcode, IMessage message, Packet.Types.Context destination)
        {
            await EnsureConnectionIsEstablished();

            Packet packet = new Packet();
            packet.Opcode = opcode;
            packet.Body = message.ToByteString();
            packet.Context = destination;

            await _client.Send(packet);
        }

        public async Task Disconnect()
        {
            if (!isConnectedToGateway) {
                return;
            }

            await _gateway.Close();

           isConnectedToGateway = false; 
        }

        private async Task EnsureConnectionIsEstablished()
        {
            if(isConnectedToGateway) {
                return;
            }

            await _gateway.ConnectTo(_gatewayHost, _gatewayPort);

            isConnectedToGateway = true;
        }
    }
}
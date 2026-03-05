using System;
using System.Collections.Concurrent;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;
using Fr.Raven.Proto.Message.Gateway;
using Google.Protobuf;
using UnityEngine;

namespace Core.Networking.Gateway
{
    /// <summary>
    /// Manages the TCP connection to the gateway server.
    /// Uses a background thread for reading and a ConcurrentQueue for thread-safe packet delivery.
    /// </summary>
    public class GatewayConnection : IPacketSender, IPacketReceiver, IDisposable
    {
        private readonly string _host;
        private readonly int _port;

        private TcpClient _tcpClient;
        private NetworkStream _stream;
        private Thread _readThread;
        private CancellationTokenSource _cts;

        private readonly ConcurrentQueue<Packet> _incomingPackets = new ConcurrentQueue<Packet>();
        private readonly object _sendLock = new object();

        private bool _isConnected;

        public event Action<Packet> PacketReceived;

        private const int ReadBufferSize = 65536;
        private const int MaxPacketsPerFrame = 100;

        public bool IsConnected => _isConnected;

        public GatewayConnection(string host, int port)
        {
            _host = host;
            _port = port;
        }

        /// <summary>
        /// Establishes the TCP connection to the gateway and starts the background read thread.
        /// </summary>
        public async Task Connect()
        {
            if (_isConnected)
                return;

            _tcpClient = new TcpClient { NoDelay = true };
            await _tcpClient.ConnectAsync(_host, _port);
            _stream = _tcpClient.GetStream();
            _isConnected = true;

            _cts = new CancellationTokenSource();
            _readThread = new Thread(ReadLoop)
            {
                IsBackground = true,
                Name = "GatewayReadThread"
            };
            _readThread.Start();
        }

        /// <summary>
        /// Sends a protobuf message to the gateway. Thread-safe via lock.
        /// </summary>
        public async Task SendMessage(int opcode, IMessage body, Packet.Types.Context context)
        {
            await EnsureConnected();

            var packet = new Packet
            {
                Opcode = opcode,
                Body = body.ToByteString(),
                Context = context,
                CreatedAt = 1,
                Version = new Fr.Raven.Proto.Message.Gateway.Version
                {
                    MajorVersion = 1,
                    MinorVersion = 1,
                    PatchVersion = 0
                }
            };

            byte[] frame = PacketFramer.Encode(packet);

            lock (_sendLock)
            {
                _stream.Write(frame, 0, frame.Length);
            }
        }

        /// <summary>
        /// Drains the internal queue on the main thread and fires PacketReceived for each packet.
        /// </summary>
        public void ProcessIncomingPackets()
        {
            int processed = 0;

            while (_incomingPackets.TryDequeue(out Packet packet) && processed < MaxPacketsPerFrame)
            {
                processed++;

                try
                {
                    PacketReceived?.Invoke(packet);
                }
                catch (Exception ex)
                {
                    Debug.LogError($"[GatewayConnection] Error processing packet (opcode={packet.Opcode}, context={packet.Context}): {ex}");
                }
            }
        }

        /// <summary>
        /// Background thread: reads from the TCP stream, decodes length-framed packets, enqueues them.
        /// </summary>
        private void ReadLoop()
        {
            byte[] buffer = new byte[ReadBufferSize];
            int bufferCount = 0;

            try
            {
                while (!_cts.Token.IsCancellationRequested)
                {
                    if (bufferCount >= buffer.Length)
                    {
                        byte[] newBuffer = new byte[buffer.Length * 2];
                        Buffer.BlockCopy(buffer, 0, newBuffer, 0, bufferCount);
                        buffer = newBuffer;
                    }

                    int bytesRead = _stream.Read(buffer, bufferCount, buffer.Length - bufferCount);

                    if (bytesRead == 0)
                    {
                        Debug.Log("[GatewayConnection] Server closed the connection.");
                        break;
                    }

                    bufferCount += bytesRead;

                    int offset = 0;
                    while (PacketFramer.TryDecode(buffer, ref offset, bufferCount, out Packet packet))
                    {
                        _incomingPackets.Enqueue(packet);
                    }

                    bufferCount = PacketFramer.Compact(buffer, offset, bufferCount);
                }
            }
            catch (Exception ex)
            {
                if (!_cts.Token.IsCancellationRequested)
                {
                    Debug.LogError($"[GatewayConnection] Read error: {ex.Message}");
                }
            }
            finally
            {
                _isConnected = false;
            }
        }

        /// <summary>
        /// Disconnects and stops the read thread.
        /// </summary>
        public async Task Disconnect()
        {
            if (!_isConnected)
                return;

            _cts?.Cancel();

            try
            {
                _stream?.Close();
                _tcpClient?.Close();
            }
            catch (Exception ex)
            {
                Debug.LogWarning($"[GatewayConnection] Error during disconnect: {ex.Message}");
            }

            _isConnected = false;
            await Task.Delay(100);
        }

        public void Dispose()
        {
            _cts?.Cancel();
            _stream?.Dispose();
            _tcpClient?.Dispose();
            _cts?.Dispose();
            _isConnected = false;
        }

        private async Task EnsureConnected()
        {
            if (!_isConnected)
            {
                await Connect();
            }
        }
    }
}

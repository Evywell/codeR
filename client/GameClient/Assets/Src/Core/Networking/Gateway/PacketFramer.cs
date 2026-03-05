using System;
using Fr.Raven.Proto.Message.Gateway;
using Google.Protobuf;

namespace Core.Networking.Gateway
{
    /// <summary>
    /// Handles length-prefix framing for protobuf packets over a raw TCP stream.
    /// Wire format: [4-byte big-endian length][protobuf-encoded Packet]
    /// </summary>
    public static class PacketFramer
    {
        /// <summary>
        /// Encodes a gateway Packet into a length-prefixed byte array ready for TCP transmission.
        /// </summary>
        public static byte[] Encode(Packet packet)
        {
            byte[] body = packet.ToByteArray();
            byte[] frame = new byte[4 + body.Length];

            // Big-endian length prefix
            frame[0] = (byte)((body.Length >> 24) & 0xFF);
            frame[1] = (byte)((body.Length >> 16) & 0xFF);
            frame[2] = (byte)((body.Length >> 8) & 0xFF);
            frame[3] = (byte)(body.Length & 0xFF);

            Buffer.BlockCopy(body, 0, frame, 4, body.Length);

            return frame;
        }

        /// <summary>
        /// Attempts to decode one complete packet from the buffer.
        /// Returns true if a packet was decoded, false if more data is needed.
        /// </summary>
        public static bool TryDecode(byte[] buffer, ref int offset, int count, out Packet packet)
        {
            packet = null;

            int available = count - offset;

            if (available < 4)
                return false;

            int length = (buffer[offset] << 24)
                       | (buffer[offset + 1] << 16)
                       | (buffer[offset + 2] << 8)
                       | buffer[offset + 3];

            if (available < 4 + length)
                return false;

            packet = Packet.Parser.ParseFrom(buffer, offset + 4, length);
            offset += 4 + length;

            return true;
        }

        /// <summary>
        /// Compacts the buffer by moving unprocessed bytes to the beginning.
        /// </summary>
        public static int Compact(byte[] buffer, int offset, int count)
        {
            int remaining = count - offset;

            if (remaining > 0 && offset > 0)
            {
                Buffer.BlockCopy(buffer, offset, buffer, 0, remaining);
            }

            return remaining;
        }
    }
}

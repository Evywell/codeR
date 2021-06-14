package fr.rob.core.network.netty

import fr.rob.core.network.Packet
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

class NettyPacket(opcode: Int?, buffer: ByteArray?) : Packet(opcode, buffer) {

    companion object {
        fun fromByteArray(byteArray: ByteArray): Packet {
            val buffer: ByteBuf = Unpooled.copiedBuffer(byteArray)
            val bytes: ByteArray

            if (buffer.hasArray()) {
                bytes = buffer.array()
            } else {
                val length: Int = buffer.readableBytes()
                bytes = ByteArray(length)
                buffer.getBytes(buffer.readerIndex(), bytes)
            }

            return Packet.fromByteArray(bytes)
        }
    }
}

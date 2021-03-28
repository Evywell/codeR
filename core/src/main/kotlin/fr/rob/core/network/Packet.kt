package fr.rob.core.network

import java.nio.ByteBuffer

open class Packet(private var opcode: Int?, private var buffer: ByteArray?) {

    private lateinit var readableBuffer: ByteBuffer
    private var currentPos = 0

    init {
        if (buffer != null) {
            readableBuffer = ByteBuffer.wrap(buffer)
        }
    }

    companion object {
        const val INTEGER_BYTE_LENGTH = 4

        fun fromByteArray(buffer: ByteArray): Packet {
            val packet = Packet(null, buffer)
            packet.readOpcode()

            return packet
        }
    }

    fun readOpcode(): Int {
        if (opcode != null) {
            return opcode as Int
        }

        opcode = readInt()

        return opcode as Int
    }

    fun toByteArray(): ByteArray {
        return buffer!!.copyOfRange(currentPos, buffer!!.size)
    }

    private fun readInt(): Int {
        val value = readableBuffer.int
        currentPos += INTEGER_BYTE_LENGTH

        return value
    }
}
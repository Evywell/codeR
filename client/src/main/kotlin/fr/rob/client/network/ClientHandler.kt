package fr.rob.client.network

import fr.rob.core.network.Packet
import fr.rob.core.network.netty.NettyPacket
import fr.rob.core.network.netty.NettySessionSocket
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

abstract class ClientHandler(protected val client: Client) : ChannelInboundHandlerAdapter() {

    abstract fun processPacket(opcode: Int, packet: Packet)

    override fun channelActive(ctx: ChannelHandlerContext) {
        client.session = ClientSession()
        client.session.socket = NettySessionSocket(ctx.channel(), client.session, client)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            val packet = NettyPacket.fromByteArray(msg as ByteArray)
            val opcode = packet.readOpcode()

            processPacket(opcode, packet)
        } catch (exception: Exception) {
            exception.printStackTrace()

            if (ctx.channel().isOpen) {
                ctx.channel().close()
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}

package fr.rob.game.domain.network.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.socket.DatagramPacket
import java.nio.charset.Charset

class NettyUdpGameServerHandler(private val nettyGameServer: NettyUdpGameServer) : ChannelInboundHandlerAdapter() {

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        println("read 2")
        println(msg::class.qualifiedName)
        val packet = msg as DatagramPacket
        val address = packet.sender().address
        val buffer = packet.content()
        val packetLength = buffer.readableBytes()
        println("ALLOOOO")

        println("$address $packetLength ${buffer.toString(Charset.defaultCharset())}")
    }
}

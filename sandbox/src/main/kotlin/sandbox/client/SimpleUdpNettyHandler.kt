package sandbox.client

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.socket.DatagramPacket

class SimpleUdpNettyHandler : SimpleChannelInboundHandler<Any>() {
    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        println("Channel active")
        ctx.channel().writeAndFlush(1)
    }

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: Any?) {
        val packet = msg as DatagramPacket
        println(packet.content())
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val packet = msg as DatagramPacket
        println(packet.content())
    }
}

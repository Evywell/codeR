package sandbox.physicClient

import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramChannel
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.handler.codec.serialization.ObjectEncoder
import java.nio.charset.Charset

class PhysicClient(private val port: Int) {
    private var channel: Channel? = null
    private var workGroup: EventLoopGroup = NioEventLoopGroup()

    @Throws(Exception::class)
    fun startup(): ChannelFuture {
        try {
            val b = Bootstrap()
            b.group(workGroup)
            b.channel(NioDatagramChannel::class.java)
            b.handler(object : ChannelInitializer<DatagramChannel>() {
                @Throws(Exception::class)
                override fun initChannel(datagramChannel: DatagramChannel) {
                    datagramChannel.pipeline()
                        .addLast(ObjectEncoder())
                        .addLast(PhysicClientHandler())
                }
            })
            val channelFuture: ChannelFuture = b.connect("127.0.0.1", port).sync()
            channel = channelFuture.channel()

            return channelFuture
        } catch (e: Exception) {
            e.printStackTrace()
        }

        throw Exception("Nothing returned")
    }

    fun shutdown() {
        workGroup.shutdownGracefully()
    }

    class PhysicClientHandler : SimpleChannelInboundHandler<Any>() {
        @Throws(java.lang.Exception::class)
        override fun channelReadComplete(ctx: ChannelHandlerContext?) {
            super.channelReadComplete(ctx)
        }

        @Throws(java.lang.Exception::class)
        override fun channelRead0(ctx: ChannelHandlerContext?, msg: Any) {
            val packet: DatagramPacket = msg as DatagramPacket
            val message: String = packet.content().toString(Charset.defaultCharset())
            println("Received Message : $message")
        }

        @Throws(java.lang.Exception::class)
        override fun channelActive(ctx: ChannelHandlerContext) {
            super.channelActive(ctx)
        }
    }
}

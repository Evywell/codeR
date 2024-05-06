package sandbox.client

import fr.rob.core.network.v2.ClientProcessInterface
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramChannel
import io.netty.channel.socket.nio.NioDatagramChannel

class SimpleUdpNettyClient(private val host: String, private val port: Int) : ClientProcessInterface {
    lateinit var channel: Channel

    override fun start() {
        val group: EventLoopGroup = NioEventLoopGroup()
        val b = Bootstrap()
        b.group(group)
        b.channel(NioDatagramChannel::class.java)
        b.handler(object : ChannelInitializer<DatagramChannel>() {
            @Throws(Exception::class)
            override fun initChannel(datagramChannel: DatagramChannel) {
                datagramChannel.pipeline().addLast(SimpleUdpNettyHandler())
            }
        })
        val channelFuture: ChannelFuture = b.connect(host, port).sync()
        channel = channelFuture.channel()
    }
}

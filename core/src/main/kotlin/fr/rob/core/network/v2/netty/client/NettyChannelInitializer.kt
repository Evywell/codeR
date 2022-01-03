package fr.rob.core.network.v2.netty.client

import fr.rob.core.network.v2.ClientInterface
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.bytes.ByteArrayDecoder
import io.netty.handler.codec.bytes.ByteArrayEncoder

class NettyChannelInitializer(private val client: ClientInterface) : ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        ch.pipeline().addLast(
            LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4)
        )

        ch.pipeline().addLast(ByteArrayDecoder())
        ch.pipeline().addLast(LengthFieldPrepender(4))
        ch.pipeline().addLast(ByteArrayEncoder())
        ch.pipeline().addLast(NettyChannelHandler(client))
    }
}

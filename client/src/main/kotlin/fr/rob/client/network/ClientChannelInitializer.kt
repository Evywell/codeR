package fr.rob.client.network

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.bytes.ByteArrayDecoder
import io.netty.handler.codec.bytes.ByteArrayEncoder

class ClientChannelInitializer(private val client: Client) : ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        ch.pipeline().addLast(
            LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4)
        )

        ch.pipeline().addLast(ByteArrayDecoder())
        ch.pipeline().addLast(LengthFieldPrepender(4))
        ch.pipeline().addLast(ByteArrayEncoder())
        ch.pipeline().addLast(client.clientHandler)
    }
}

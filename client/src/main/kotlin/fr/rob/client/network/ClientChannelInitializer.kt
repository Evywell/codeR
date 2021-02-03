package fr.rob.client.network

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.bytes.ByteArrayDecoder
import io.netty.handler.codec.bytes.ByteArrayEncoder
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder


class ClientChannelInitializer(private val client: Client) : ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        ch.pipeline().addLast(
            "frameDecoder",
            LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4)
        )

        ch.pipeline().addLast("decoder", ByteArrayDecoder())
        ch.pipeline().addLast("frameEncoder", LengthFieldPrepender(4))
        ch.pipeline().addLast("bytesEncoder", ByteArrayEncoder())
        ch.pipeline().addLast(ClientHandler(client))
    }
}

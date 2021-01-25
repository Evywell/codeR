package fr.rob.client.network

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder


class ClientChannelInitializer() : ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        // Configure encoders/decoder or codecs
        ch.pipeline().addLast(StringDecoder())
        ch.pipeline().addLast(StringEncoder())
        // Add Custom Inbound handler to handle incoming traffic
        ch.pipeline().addLast(ClientHandler())
    }
}
package fr.rob.game.domain.network.netty

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.bytes.ByteArrayDecoder
import io.netty.handler.codec.bytes.ByteArrayEncoder
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate

class NettyServerInitializer(private val nettyGameServer: NettyGameServer, private val ssl: Boolean = false) :
    ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        if (ssl) {
            val selfSignedCertificate = SelfSignedCertificate()
            val sslCtx = SslContextBuilder
                .forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey())
                .build()
            pipeline.addLast(sslCtx.newHandler(ch.alloc()))
        }

        pipeline.addLast(
            "frameDecoder",
            LengthFieldBasedFrameDecoder(
                1048576,
                0,
                4,
                0,
                4
            )
        )

        pipeline.addLast("decoder", ByteArrayDecoder())
        pipeline.addLast("frameEncoder", LengthFieldPrepender(4))
        pipeline.addLast("bytesEncoder", ByteArrayEncoder())
        pipeline.addLast(NettyGameServerHandler(nettyGameServer))
    }
}

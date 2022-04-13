package fr.rob.core.network.v2.netty

import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate

abstract class NettyChannelInitializer<T>(protected val ssl: Boolean = false) :
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

        registerHandlers(pipeline)
        pipeline.addLast(channelHandler())
    }

    protected abstract fun registerHandlers(pipeline: ChannelPipeline)

    protected abstract fun channelHandler(): NettyChannelHandler<T>
}

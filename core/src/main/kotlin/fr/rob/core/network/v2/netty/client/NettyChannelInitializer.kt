package fr.rob.core.network.v2.netty.client

import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel

abstract class NettyChannelInitializer<T> : ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        registerHandlers(pipeline)
        pipeline.addLast(channelHandler())
    }

    protected abstract fun registerHandlers(pipeline: ChannelPipeline)
    protected abstract fun channelHandler(): NettyChannelHandler<T>
}

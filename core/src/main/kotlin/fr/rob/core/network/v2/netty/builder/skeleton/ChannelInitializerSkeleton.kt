package fr.rob.core.network.v2.netty.builder.skeleton

import fr.rob.core.network.v2.netty.NettyChannelHandler
import fr.rob.core.network.v2.netty.NettyChannelInitializer
import fr.rob.core.network.v2.netty.builder.shard.HandlerShardInterface
import fr.rob.core.network.v2.netty.builder.shard.PipelineShardInterface
import io.netty.channel.ChannelPipeline

class ChannelInitializerSkeleton<T>(
    private val pipelineShard: PipelineShardInterface,
    private val handlerShard: HandlerShardInterface<T>,
    ssl: Boolean
) : NettyChannelInitializer<T>(ssl) {
    override fun registerHandlers(pipeline: ChannelPipeline) {
        pipelineShard.registerHandlers(pipeline)
    }

    override fun channelHandler(): NettyChannelHandler<T> = handlerShard.channelHandler()
}

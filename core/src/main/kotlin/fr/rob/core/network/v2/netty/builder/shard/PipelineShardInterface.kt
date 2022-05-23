package fr.rob.core.network.v2.netty.builder.shard

import io.netty.channel.ChannelPipeline

interface PipelineShardInterface {
    fun registerHandlers(pipeline: ChannelPipeline)
}

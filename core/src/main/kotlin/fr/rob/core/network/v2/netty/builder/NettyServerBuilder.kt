package fr.rob.core.network.v2.netty.builder

import fr.rob.core.network.v2.ServerProcessInterface
import fr.rob.core.network.v2.netty.builder.shard.ConfigShardInterface
import fr.rob.core.network.v2.netty.builder.shard.HandlerShardInterface
import fr.rob.core.network.v2.netty.builder.shard.PipelineShardInterface
import fr.rob.core.network.v2.netty.builder.skeleton.ChannelInitializerSkeleton
import fr.rob.core.network.v2.netty.builder.skeleton.NettyServerSkeleton

class NettyServerBuilder<T>(private val port: Int, private val ssl: Boolean) {
    fun build(
        configShard: ConfigShardInterface,
        pipelineShard: PipelineShardInterface,
        handlerShard: HandlerShardInterface<T>
    ): ServerProcessInterface {
        val initializer = ChannelInitializerSkeleton(pipelineShard, handlerShard, ssl)

        return NettyServerSkeleton(configShard, initializer, port)
    }
}

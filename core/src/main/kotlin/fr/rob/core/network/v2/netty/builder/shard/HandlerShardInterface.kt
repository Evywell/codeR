package fr.rob.core.network.v2.netty.builder.shard

import fr.rob.core.network.v2.netty.NettyChannelHandler

interface HandlerShardInterface<T> {
    fun channelHandler(): NettyChannelHandler<T>
}

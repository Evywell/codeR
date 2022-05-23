package fr.rob.core.network.v2.netty.shard

import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelHandler
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilderInterface
import fr.rob.core.network.v2.netty.builder.shard.HandlerShardInterface
import fr.rob.core.network.v2.netty.protobuf.ProtobufNettyChannelHandler

class ProtobufHandlerShard<T>(
    private val server: ServerInterface<T>,
    private val socketBuilder: NettySessionSocketBuilderInterface
) : HandlerShardInterface<T> {
    override fun channelHandler(): NettyChannelHandler<T> =
        ProtobufNettyChannelHandler(server, socketBuilder)
}

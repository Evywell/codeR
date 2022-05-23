package fr.rob.core.network.v2.netty.shard

import fr.rob.core.network.v2.netty.builder.shard.ConfigShardInterface
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.ServerSocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

class NioConfigShard : ConfigShardInterface {
    override fun getLoopGroup(): EventLoopGroup = NioEventLoopGroup()

    override fun getWorkerGroup(): EventLoopGroup = NioEventLoopGroup()

    override fun getServerSocketClass(): Class<out ServerSocketChannel> = NioServerSocketChannel::class.java
}

package fr.rob.core.network.v2.netty.shard

import fr.rob.core.network.v2.netty.builder.shard.ConfigShardInterface
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.socket.ServerSocketChannel

class EpollConfigShard : ConfigShardInterface {
    override fun getLoopGroup(): EventLoopGroup = EpollEventLoopGroup()

    override fun getWorkerGroup(): EventLoopGroup = EpollEventLoopGroup()

    override fun getServerSocketClass(): Class<out ServerSocketChannel> = EpollServerSocketChannel::class.java
}

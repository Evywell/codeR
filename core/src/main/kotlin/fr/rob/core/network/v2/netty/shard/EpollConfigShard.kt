package fr.rob.core.network.v2.netty.shard

import fr.rob.core.network.v2.netty.builder.shard.ConfigShardInterface
import io.netty.channel.EventLoopGroup
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.epoll.EpollIoHandler
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.socket.ServerSocketChannel

class EpollConfigShard : ConfigShardInterface {
    override fun getLoopGroup(): EventLoopGroup = MultiThreadIoEventLoopGroup(EpollIoHandler.newFactory())

    override fun getWorkerGroup(): EventLoopGroup = MultiThreadIoEventLoopGroup(EpollIoHandler.newFactory())

    override fun getServerSocketClass(): Class<out ServerSocketChannel> = EpollServerSocketChannel::class.java
}

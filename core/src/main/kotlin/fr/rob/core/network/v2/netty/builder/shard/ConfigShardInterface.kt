package fr.rob.core.network.v2.netty.builder.shard

import io.netty.channel.EventLoopGroup
import io.netty.channel.socket.ServerSocketChannel

interface ConfigShardInterface {
    fun getLoopGroup(): EventLoopGroup
    fun getWorkerGroup(): EventLoopGroup
    fun getServerSocketClass(): Class<out ServerSocketChannel>
}

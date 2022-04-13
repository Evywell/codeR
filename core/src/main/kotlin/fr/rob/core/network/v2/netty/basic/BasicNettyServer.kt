package fr.rob.core.network.v2.netty.basic

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelInitializer
import fr.rob.core.network.v2.netty.NettyServer

class BasicNettyServer(
    port: Int,
    private val server: ServerInterface<Packet>,
    ssl: Boolean
) : NettyServer<Packet>(port, ssl) {
    override fun channelInitializer(): NettyChannelInitializer<Packet> = BasicNettyChannelInitializer(server, ssl)
}

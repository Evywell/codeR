package fr.rob.gateway.network.netty

import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelInitializer
import fr.rob.core.network.v2.netty.NettyServer
import fr.rob.gateway.message.GatewayProto.Packet

class NettyServer(port: Int, private val server: ServerInterface<Packet>, ssl: Boolean) :
    NettyServer<Packet>(port, ssl) {
    override fun channelInitializer(): NettyChannelInitializer<Packet> = NettyChannelInitializer(server, ssl)
}

package fr.rob.core.network.v2.netty.protobuf

import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelHandler
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilderInterface

class ProtobufNettyChannelHandler<T>(
    server: ServerInterface<T>,
    nettySessionSocketBuilder: NettySessionSocketBuilderInterface
) : NettyChannelHandler<T>(server, nettySessionSocketBuilder) {
    @Suppress("UNCHECKED_CAST")
    override fun createPacketFromMessage(msg: Any): T = msg as T
}

package fr.rob.gateway.network.netty

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelHandler
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilderInterface

class NettyChannelHandler(
    server: ServerInterface<Packet>,
    nettySessionSocketBuilder: NettySessionSocketBuilderInterface
) : NettyChannelHandler<Packet>(server, nettySessionSocketBuilder) {
    override fun createPacketFromMessage(msg: Any): Packet = msg as Packet
}

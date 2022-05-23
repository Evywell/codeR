package fr.rob.gateway.network.netty

import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelHandler
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilderInterface
import fr.rob.gateway.message.GatewayProto.Packet

class NettyChannelHandler(
    server: ServerInterface<Packet>,
    nettySessionSocketBuilder: NettySessionSocketBuilderInterface
) : NettyChannelHandler<Packet>(server, nettySessionSocketBuilder) {
    override fun createPacketFromMessage(msg: Any): Packet = msg as Packet
}

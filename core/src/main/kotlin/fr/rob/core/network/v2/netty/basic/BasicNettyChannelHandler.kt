package fr.rob.core.network.v2.netty.basic

import fr.rob.core.network.Packet
import fr.rob.core.network.netty.NettyPacket
import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelHandler
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilderInterface

class BasicNettyChannelHandler(
    server: ServerInterface<Packet>,
    nettySessionSocketBuilder: NettySessionSocketBuilderInterface
) : NettyChannelHandler<Packet>(server, nettySessionSocketBuilder) {
    override fun createPacketFromMessage(msg: Any): Packet = NettyPacket.fromByteArray(msg as ByteArray)
}

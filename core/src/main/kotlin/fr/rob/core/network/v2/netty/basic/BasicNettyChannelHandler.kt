package fr.rob.core.network.v2.netty.basic

import fr.rob.core.network.Packet
import fr.rob.core.network.netty.NettyPacket
import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelHandler
import fr.rob.core.network.v2.session.SessionSocketInterface
import io.netty.channel.ChannelHandlerContext

class BasicNettyChannelHandler(server: ServerInterface<Packet>) : NettyChannelHandler<Packet>(server) {
    override fun createPacketFromMessage(msg: Any): Packet = NettyPacket.fromByteArray(msg as ByteArray)
    override fun createSessionSocket(ctx: ChannelHandlerContext): SessionSocketInterface =
        BasicNettySessionSocket(ctx.channel())
}

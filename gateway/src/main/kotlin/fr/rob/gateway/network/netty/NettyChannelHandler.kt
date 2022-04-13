package fr.rob.gateway.network.netty

import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelHandler
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.message.GatewayProto.Packet
import io.netty.channel.ChannelHandlerContext

class NettyChannelHandler(server: ServerInterface<Packet>) : NettyChannelHandler<Packet>(server) {
    override fun createPacketFromMessage(msg: Any): Packet = msg as Packet
    override fun createSessionSocket(ctx: ChannelHandlerContext): SessionSocketInterface =
        NettySessionSocket(ctx.channel())
}

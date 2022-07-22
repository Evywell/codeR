package fr.rob.gateway.network.netty.client

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.netty.client.NettyChannelHandler
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.network.netty.NettySessionSocket
import io.netty.channel.ChannelHandlerContext

class NettyChannelHandler(client: ClientInterface<Packet>) : NettyChannelHandler<Packet>(client) {
    override fun createPacketFromMessage(msg: Any): Packet = msg as Packet

    override fun createSessionSocket(ctx: ChannelHandlerContext): SessionSocketInterface =
        NettySessionSocket(ctx.channel())
}

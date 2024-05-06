package fr.rob.game.infra.network.physic.unity

import fr.raven.proto.message.physicbridge.PhysicProto.Packet
import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.netty.NettySessionSocket
import fr.rob.core.network.v2.netty.client.NettyChannelHandler
import fr.rob.core.network.v2.session.SessionSocketInterface
import io.netty.channel.ChannelHandlerContext

class UnityTcpHandler(client: ClientInterface<Packet>) : NettyChannelHandler<Packet>(client) {
    override fun createPacketFromMessage(msg: Any): Packet = msg as Packet

    override fun createSessionSocket(ctx: ChannelHandlerContext): SessionSocketInterface =
        NettySessionSocket(ctx.channel())
}

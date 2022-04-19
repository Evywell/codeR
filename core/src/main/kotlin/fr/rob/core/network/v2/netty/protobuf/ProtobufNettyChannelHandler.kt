package fr.rob.core.network.v2.netty.protobuf

import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelHandler
import fr.rob.core.network.v2.netty.NettySessionSocket
import fr.rob.core.network.v2.session.SessionSocketInterface
import io.netty.channel.ChannelHandlerContext

class ProtobufNettyChannelHandler<T>(server: ServerInterface<T>) : NettyChannelHandler<T>(server) {
    override fun createPacketFromMessage(msg: Any): T = msg as T
    override fun createSessionSocket(ctx: ChannelHandlerContext): SessionSocketInterface =
        NettySessionSocket(ctx.channel())
}

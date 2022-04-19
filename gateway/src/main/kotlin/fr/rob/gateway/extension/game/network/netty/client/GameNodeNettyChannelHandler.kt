package fr.rob.gateway.extension.game.network.netty.client

import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.netty.client.NettyChannelHandler
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.network.netty.NettySessionSocket
import io.netty.channel.ChannelHandlerContext
import fr.rob.gateway.message.extension.game.GameProto.Packet as GamePacket

class GameNodeNettyChannelHandler(client: ClientInterface<GamePacket>) : NettyChannelHandler<GamePacket>(client) {
    override fun createPacketFromMessage(msg: Any): GamePacket = msg as GamePacket

    override fun createSessionSocket(ctx: ChannelHandlerContext): SessionSocketInterface =
        NettySessionSocket(ctx.channel())
}

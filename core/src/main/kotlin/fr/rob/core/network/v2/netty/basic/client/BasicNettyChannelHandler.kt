package fr.rob.core.network.v2.netty.basic.client

import fr.rob.core.network.Packet
import fr.rob.core.network.netty.NettyPacket
import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.netty.NettySessionSocket
import fr.rob.core.network.v2.netty.client.NettyChannelHandler
import fr.rob.core.network.v2.session.SessionSocketInterface
import io.netty.channel.ChannelHandlerContext

class BasicNettyChannelHandler(client: ClientInterface<Packet>) : NettyChannelHandler<Packet>(client) {
    override fun createPacketFromMessage(msg: Any): Packet = NettyPacket.fromByteArray(msg as ByteArray)

    override fun createSessionSocket(ctx: ChannelHandlerContext): SessionSocketInterface =
        NettySessionSocket(ctx.channel())
}

package fr.rob.client.network

import fr.rob.core.network.netty.NettySession
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class ClientHandler(private val client: Client) : ChannelInboundHandlerAdapter() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        client.session = NettySession(ctx.channel(), client.logger)
    }

}

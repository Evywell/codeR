package fr.rob.client.network

import fr.rob.game.domain.network.session.AppSession
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class ClientHandler(private val client: Client) : ChannelInboundHandlerAdapter() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        client.session = AppSession(ctx.channel())
    }

}

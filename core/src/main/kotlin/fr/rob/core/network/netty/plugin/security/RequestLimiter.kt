package fr.rob.core.network.netty.plugin.security

import fr.rob.core.network.netty.NettyPlugin
import fr.rob.core.network.netty.NettyServer
import fr.rob.core.network.netty.event.NettyChannelReadEvent
import fr.rob.core.network.netty.event.NettySessionKickedEvent

class RequestLimiter : NettyPlugin() {

    override fun boot(nettyServer: NettyServer) {
        if (rules.isEmpty()) {
            return
        }

        val listener = RuleApplierListener(this)

        nettyServer.registerListener(NettyChannelReadEvent.NETTY_CHANNEL_READ, listener)
        nettyServer.registerListener(NettySessionKickedEvent.NETTY_SESSION_KICKED, listener)
    }
}

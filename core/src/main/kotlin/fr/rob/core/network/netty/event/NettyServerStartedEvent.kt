package fr.rob.core.network.netty.event

import fr.rob.core.event.Event

class NettyServerStartedEvent : Event() {

    override fun getName(): String = NETTY_SERVER_STARTED

    companion object {
        const val NETTY_SERVER_STARTED = "NettyServerStartedEvent"
    }
}

package fr.rob.core.network.netty.event

import fr.rob.core.event.Event
import fr.rob.core.network.session.Session

class NettySessionKickedEvent(val session: Session) : Event() {

    override fun getName(): String = NETTY_SESSION_KICKED

    companion object {
        const val NETTY_SESSION_KICKED = "NettySessionKicked"
    }
}

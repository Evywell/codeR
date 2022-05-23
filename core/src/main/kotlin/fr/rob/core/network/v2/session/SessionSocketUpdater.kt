package fr.rob.core.network.v2.session

import fr.rob.core.network.v2.netty.NettyBufferedSessionSocket
import java.util.Collections

class SessionSocketUpdater {
    private val sockets = Collections.synchronizedList(ArrayList<NettyBufferedSessionSocket>())

    fun run() {
        update()
    }

    fun addSocket(socket: NettyBufferedSessionSocket) {
        if (sockets.contains(socket)) {
            return
        }

        sockets.add(socket)
    }

    fun update() {
        sockets.removeIf { socket -> !socket.update() }

        Thread.sleep(1)

        update()
    }
}

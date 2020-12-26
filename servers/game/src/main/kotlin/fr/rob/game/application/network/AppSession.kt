package fr.rob.game.application.network

import fr.rob.game.domain.server.GameServer
import fr.rob.game.domain.server.Session
import fr.rob.game.domain.server.packet.Packet
import io.netty.channel.Channel
import io.netty.util.NetUtil
import java.net.InetSocketAddress

class AppSession(gameServer: GameServer, private val channel: Channel) : Session(gameServer) {

    override fun getIp(): String {
        val address = channel.remoteAddress() as InetSocketAddress

        return NetUtil.bytesToIpAddress(address.address.address)
    }

    override fun send(packet: Packet) {
        TODO("Not yet implemented: waiting for Routing")
    }
}
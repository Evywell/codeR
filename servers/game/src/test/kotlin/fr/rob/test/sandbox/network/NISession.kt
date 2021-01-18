package fr.rob.test.sandbox.network

import fr.rob.game.domain.network.GameServer
import fr.rob.game.domain.network.packet.Packet
import fr.rob.game.domain.network.session.Session

class NISession(gameServer: GameServer) : Session(gameServer) {

    override fun getIp(): String = ""

    override fun send(packet: Packet) = Unit
}

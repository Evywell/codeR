package fr.rob.test.sandbox.network

import fr.rob.game.domain.server.GameServer
import fr.rob.game.domain.server.Session
import fr.rob.game.domain.server.packet.Packet

class NISession(gameServer: GameServer) : Session(gameServer) {

    override fun getIp(): String = ""

    override fun send(packet: Packet) = Unit
}
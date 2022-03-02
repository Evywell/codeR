package fr.rob.game.network

import fr.raven.log.LoggerInterface
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.network.v2.Server
import fr.rob.game.network.session.GameSession
import fr.rob.game.opcode.GameNodeOpcodeHandler

class GameNodeServer(private val logger: LoggerInterface) : Server() {

    override fun onPacketReceived(session: Session, packet: Packet) {
        TODO("Not yet implemented")
    }

    override fun createSession(): Session = GameSession(GameNodeOpcodeHandler(logger))
}

package fr.rob.game.infra.network.session

import fr.rob.game.domain.player.session.GameSessionUpdaterInterface
import fr.rob.game.infra.network.packet.GamePacketFilter

class GameSessionUpdater : GameSessionUpdaterInterface {
    private val gatewayGameSessions = ArrayList<GatewayGameSession>()

    override fun update(deltaTime: Long) {
        gatewayGameSessions.forEach { it.update(GamePacketFilter(it)) }
    }

    fun addSession(session: GatewayGameSession) {
        gatewayGameSessions.add(session)
    }
}

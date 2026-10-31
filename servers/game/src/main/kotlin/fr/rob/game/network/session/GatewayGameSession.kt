package fr.rob.game.network.session

import fr.raven.log.LoggerInterface
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.game.player.session.GameSession
import java.util.concurrent.ConcurrentHashMap

class GatewayGameSession(
    private val logger: LoggerInterface,
    socket: SessionSocketInterface,
) : Session(socket) {
    private val playerGameSessions = ConcurrentHashMap<Int, GameSession>()

    fun findOrCreateGameSession(accountId: Int): GameSession =
        playerGameSessions.computeIfAbsent(accountId) { GameSession(it, GatewaySessionMessageSender(this, logger)) }

    fun getGameSessions(): List<GameSession> = playerGameSessions.values.toList()

    fun removeGameSessionFromAccountId(accountId: Int) {
        playerGameSessions.remove(accountId)
    }
}

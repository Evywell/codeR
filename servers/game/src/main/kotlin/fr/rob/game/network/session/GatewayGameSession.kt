package fr.rob.game.network.session

import fr.raven.log.LoggerInterface
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.game.player.session.GameSession
import fr.rob.game.network.session.GameSessionAlreadyOpenedException
import fr.rob.game.network.session.GameSessionNotFoundException
import fr.rob.game.network.session.GatewaySessionMessageSender

class GatewayGameSession(
    private val logger: LoggerInterface,
    socket: SessionSocketInterface,
) : Session(socket) {
    private val playerGameSessions = HashMap<Int, GameSession>()

    fun findGameSession(accountId: Int): GameSession = playerGameSessions[accountId]
        ?: throw GameSessionNotFoundException("Cannot find game session with accountId $accountId")

    fun createGameSession(accountId: Int): GameSession {
        if (playerGameSessions.containsKey(accountId)) {
            throw GameSessionAlreadyOpenedException("The game session is already opened for account $accountId")
        }

        val messageSender = GatewaySessionMessageSender(this, logger)
        val gameSession = GameSession(accountId, messageSender)

        playerGameSessions[accountId] = gameSession

        return gameSession
    }

    fun removeGameSessionFromAccountId(accountId: Int) {
        playerGameSessions.remove(accountId)
    }
}

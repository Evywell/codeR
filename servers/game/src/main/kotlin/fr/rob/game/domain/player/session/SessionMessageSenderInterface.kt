package fr.rob.game.domain.player.session

interface SessionMessageSenderInterface {
    fun send(session: GameSession, message: GameMessageHolder)
}

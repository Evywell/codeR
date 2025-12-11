package fr.rob.game.player.session

interface SessionMessageSenderInterface {
    fun send(session: GameSession, message: GameMessageHolder)
}

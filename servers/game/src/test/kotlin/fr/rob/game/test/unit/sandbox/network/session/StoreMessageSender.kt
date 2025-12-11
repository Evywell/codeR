package fr.rob.game.test.unit.sandbox.network.session

import fr.rob.game.player.session.GameMessageHolder
import fr.rob.game.player.session.GameSession
import fr.rob.game.player.session.SessionMessageSenderInterface

class StoreMessageSender : SessionMessageSenderInterface {
    private val messagesSent = ArrayList<MessageSent>()

    override fun send(session: GameSession, message: GameMessageHolder) {
        messagesSent.add(MessageSent(session, message))
    }

    fun getMessages(): List<MessageSent> = messagesSent

    data class MessageSent(val session: GameSession, val message: GameMessageHolder)
}

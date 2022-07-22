package fr.rob.game.test.unit.sandbox.network.session

import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.player.session.SessionMessageSenderInterface

class NullMessageSender : SessionMessageSenderInterface {
    override fun send(session: GameSession, message: GameMessageHolder) { }
}

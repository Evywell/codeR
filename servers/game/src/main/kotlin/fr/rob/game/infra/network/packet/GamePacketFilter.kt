package fr.rob.game.infra.network.packet

import fr.rob.core.network.Filter
import fr.rob.game.domain.player.session.GameSession

class GamePacketFilter : Filter<GameSession.MessageHolder>() {

    override fun process(subject: GameSession.MessageHolder): Boolean {
        // @todo add !session.findGameSession(subject.packet.getSender()).player || !session.player.isInWorld()
        return true
    }
}

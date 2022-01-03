package fr.rob.game.network

import fr.rob.core.network.Filter
import fr.rob.core.network.Packet
import fr.rob.game.network.session.GameSession

class GamePacketFilter(private val session: GameSession) : Filter<Packet>() {

    override fun process(subject: Packet): Boolean {
		// @todo add !session.player || !session.player.isInWorld()		
		return true
    }
}

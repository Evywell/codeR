package fr.rob.game.infra.network.packet

import fr.rob.core.network.Filter
import fr.rob.game.infra.network.session.GatewayGameSession
import fr.rob.game.infra.network.session.GatewayGameSession.PacketHolder

class GamePacketFilter(private val session: GatewayGameSession) : Filter<PacketHolder>() {

    override fun process(subject: PacketHolder): Boolean {
        // @todo add !session.findGameSession(subject.packet.getSender()).player || !session.player.isInWorld()
        return true
    }
}

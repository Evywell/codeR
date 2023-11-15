package fr.rob.game.domain.world.packet

import fr.rob.core.network.Filter

class WorldPacketFilter : Filter<WorldPacket>() {
    override fun process(subject: WorldPacket): Boolean {
        return true
    }
}

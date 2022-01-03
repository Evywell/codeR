package fr.rob.game.network.session

import fr.rob.core.network.Filter
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.network.thread.LockedQueue
import fr.rob.core.opcode.OpcodeHandlerInterface

class GameSession(private val opcodeHandler: OpcodeHandlerInterface) : Session() {

    private val queue = LockedQueue<Packet>()

    fun update(deltaTime: Int, filter: Filter<Packet>) {
        var nextPacket = queue.next(filter)
        var packetCount = 0

        while (nextPacket != null) {
            ++packetCount
            opcodeHandler.process(nextPacket.readOpcode(), this, nextPacket)

            if (packetCount > MAX_PACKET_PROCESSING_ON_UPDATE) {
                break
            }

            nextPacket = queue.next(filter)
        }
    }

    companion object {
        private const val MAX_PACKET_PROCESSING_ON_UPDATE = 200
    }
}

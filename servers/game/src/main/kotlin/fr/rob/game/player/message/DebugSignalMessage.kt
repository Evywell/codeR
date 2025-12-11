package fr.rob.game.player.message

import fr.rob.game.player.session.GameMessageHolder
import fr.rob.game.player.session.GameMessageInterface
import fr.rob.game.network.opcode.SMSG_DEBUG_SIGNAL

data class DebugSignalMessage(val signalName: String, val signalValue: Int) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_DEBUG_SIGNAL, this)
}

package fr.rob.game.app.player.message

import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameMessageInterface
import fr.rob.game.infra.opcode.SMSG_DEBUG_SIGNAL

data class DebugSignalMessage(val signalName: String, val signalValue: Int) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_DEBUG_SIGNAL, this)
}

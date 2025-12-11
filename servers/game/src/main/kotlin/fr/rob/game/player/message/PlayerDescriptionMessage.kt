package fr.rob.game.player.message

import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.player.session.GameMessageHolder
import fr.rob.game.player.session.GameMessageInterface
import fr.rob.game.network.opcode.SMSG_PLAYER_DESCRIPTION

data class PlayerDescriptionMessage(val guid: ObjectGuid, val name: String) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_PLAYER_DESCRIPTION, this)
}

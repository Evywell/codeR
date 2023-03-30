package fr.rob.game.app.player.message

import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameMessageInterface
import fr.rob.game.infra.opcode.SMSG_PLAYER_DESCRIPTION

data class PlayerDescriptionMessage(val guid: ObjectGuid, val name: String) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_PLAYER_DESCRIPTION, this)
}

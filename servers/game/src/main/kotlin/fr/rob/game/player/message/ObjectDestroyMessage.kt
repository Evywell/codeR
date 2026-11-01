package fr.rob.game.player.message

import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.network.opcode.SMSG_OBJECT_DESTROY
import fr.rob.game.player.session.GameMessageHolder
import fr.rob.game.player.session.GameMessageInterface

data class ObjectDestroyMessage(val objectId: ObjectGuid) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_OBJECT_DESTROY, this)
}

package fr.rob.game.player.message

import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.player.session.GameMessageHolder
import fr.rob.game.player.session.GameMessageInterface
import fr.rob.game.network.opcode.SMSG_OBJECT_HEALTH_UPDATED

data class HealthMessage(val objectId: ObjectGuid, val health: Int) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_OBJECT_HEALTH_UPDATED, this)
}

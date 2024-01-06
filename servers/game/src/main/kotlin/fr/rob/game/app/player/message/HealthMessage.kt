package fr.rob.game.app.player.message

import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameMessageInterface
import fr.rob.game.infra.opcode.SMSG_OBJECT_HEALTH_UPDATED

data class HealthMessage(val objectId: ObjectGuid, val health: Int) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_OBJECT_HEALTH_UPDATED, this)
}

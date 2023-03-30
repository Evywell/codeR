package fr.rob.game.app.player.message

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameMessageInterface
import fr.rob.game.infra.opcode.SMSG_MOVEMENT_HEARTBEAT

data class MovementHeartbeatMessage(
    val objectId: ObjectGuid,
    val position: Position
) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_MOVEMENT_HEARTBEAT, this)
}

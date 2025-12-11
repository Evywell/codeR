package fr.rob.game.player.message

import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.movement.Movable
import fr.rob.game.player.session.GameMessageHolder
import fr.rob.game.player.session.GameMessageInterface
import fr.rob.game.network.opcode.SMSG_MOVEMENT_HEARTBEAT

data class MovementHeartbeatMessage(
    val objectId: ObjectGuid,
    val position: Position,
    val movement: Movable.Movement?
) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_MOVEMENT_HEARTBEAT, this)
}

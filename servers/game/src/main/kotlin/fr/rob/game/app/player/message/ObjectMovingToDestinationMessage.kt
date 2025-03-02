package fr.rob.game.app.player.message

import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.maths.Vector3f
import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameMessageInterface
import fr.rob.game.infra.opcode.SMSG_OBJECT_MOVING_TO_DESTINATION

class ObjectMovingToDestinationMessage(
    val objectId: ObjectGuid,
    val destination: Vector3f
) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_OBJECT_MOVING_TO_DESTINATION, this)
}
package fr.rob.game.player.message

import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.map.maths.Vector3f
import fr.rob.game.player.session.GameMessageHolder
import fr.rob.game.player.session.GameMessageInterface
import fr.rob.game.network.opcode.SMSG_OBJECT_MOVING_TO_DESTINATION

class ObjectMovingToDestinationMessage(
    val objectId: ObjectGuid,
    val destination: Vector3f
) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_OBJECT_MOVING_TO_DESTINATION, this)
}

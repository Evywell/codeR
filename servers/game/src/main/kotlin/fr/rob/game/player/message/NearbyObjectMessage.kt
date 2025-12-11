package fr.rob.game.player.message

import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.player.session.GameMessageHolder
import fr.rob.game.player.session.GameMessageInterface
import fr.rob.game.network.opcode.SMSG_NEARBY_OBJECT_UPDATE

data class NearbyObjectMessage(
    val objectId: ObjectGuid,
    val position: Position
) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_NEARBY_OBJECT_UPDATE, this)
}

package fr.rob.game.app.player.message

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameMessageInterface
import fr.rob.game.infra.opcode.SMSG_NEARBY_OBJECT_UPDATE

data class NearbyObjectMessage(
    val objectId: ObjectGuid,
    val position: Position
) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_NEARBY_OBJECT_UPDATE, this)
}

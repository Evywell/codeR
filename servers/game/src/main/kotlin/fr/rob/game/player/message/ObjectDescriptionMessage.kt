package fr.rob.game.player.message

import fr.rob.game.entity.WorldObject
import fr.rob.game.network.opcode.SMSG_OBJECT_DESCRIPTION
import fr.rob.game.player.session.GameMessageHolder
import fr.rob.game.player.session.GameMessageInterface

data class ObjectDescriptionMessage(val worldObject: WorldObject) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_OBJECT_DESCRIPTION, this)
}
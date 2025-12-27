package fr.rob.game.app.player.message.ability

import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.network.opcode.SMSG_ABILITY_FAILED
import fr.rob.game.player.session.GameMessageHolder
import fr.rob.game.player.session.GameMessageInterface

data class AbilityFailedMessage(
    val caster: ObjectGuid,
    val abilityId: Int,
) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_ABILITY_FAILED, this)
}

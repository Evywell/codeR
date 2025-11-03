package fr.rob.game.app.player.message.ability

import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameMessageInterface
import fr.rob.game.infra.opcode.SMSG_ABILITY_FAILED

data class AbilityFailedMessage(
    val caster: ObjectGuid,
    val abilityId: Int,
) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_ABILITY_FAILED, this)
}

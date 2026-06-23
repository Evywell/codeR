package fr.rob.game.player.message

import fr.rob.game.ability.Ability
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.network.opcode.SMSG_ABILITY_STATE_UPDATE
import fr.rob.game.player.session.GameMessageHolder
import fr.rob.game.player.session.GameMessageInterface

data class AbilityStateUpdateMessage(
    val caster: ObjectGuid,
    val abilityId: Int,
    val state: Ability.AbilityState,
) : GameMessageInterface {
    override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(SMSG_ABILITY_STATE_UPDATE, this)
}

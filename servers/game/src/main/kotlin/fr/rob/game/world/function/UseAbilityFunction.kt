package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.AbilityProto
import fr.rob.game.ability.AbilityTargetParameter
import fr.rob.game.ability.ObjectAbilityManager
import fr.rob.game.ability.exception.ObjectCannotUseAbilityException
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.WorldObject
import fr.rob.game.player.session.GameSession

class UseAbilityFunction(
    private val abilityManager: ObjectAbilityManager,
) : InWorldFunctionInterface {
    override fun invoke(sender: GameSession, controlled: WorldObject, message: Message) {
        message as AbilityProto.UseAbility

        val target = controlled.mapInstance.findObjectByGuid(ObjectGuid(message.explicitTargetGuid))

        val targetGuid = target?.guid

        try {
            abilityManager.useAbilityFromIdentifier(
                controlled,
                message.abilityId,
                AbilityTargetParameter(targetGuid, controlled),
            )
        } catch (e: ObjectCannotUseAbilityException) {
            println("[ObjectCannotUseAbilityException] ${e.message}")
            // TODO: Send a formatted message to the client
            return
        }
    }

    override fun parseFromByteString(data: ByteString): Message = AbilityProto.UseAbility.parseFrom(data)
}
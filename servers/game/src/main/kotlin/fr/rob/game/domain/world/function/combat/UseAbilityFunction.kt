package fr.rob.game.domain.world.function.combat

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.AbilityProto.UseAbility
import fr.rob.game.domain.ability.AbilityTargetParameter
import fr.rob.game.domain.ability.ObjectAbilityManager
import fr.rob.game.domain.ability.exception.ObjectCannotUseAbilityException
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.world.function.WorldFunctionInterface

class UseAbilityFunction(
    private val abilityManager: ObjectAbilityManager,
) : WorldFunctionInterface {
    override fun invoke(
        sender: GameSession,
        opcode: Int,
        message: Message,
    ) {
        message as UseAbility

        val controlledWorldObject = sender.controlledWorldObject ?: return
        val target = controlledWorldObject.mapInstance.findObjectByGuid(ObjectGuid(message.explicitTargetGuid))

        val targetGuid = if (target.isPresent) target.get().guid else null

        try {
            abilityManager.useAbilityFromIdentifier(
                controlledWorldObject,
                message.abilityId,
                AbilityTargetParameter(targetGuid, controlledWorldObject),
            )
        } catch (e: ObjectCannotUseAbilityException) {
            println("[ObjectCannotUseAbilityException] ${e.message}")
            // TODO: Send a formatted message to the client
            return
        }
    }

    override fun parseFromByteString(data: ByteString): Message = UseAbility.parseFrom(data)
}

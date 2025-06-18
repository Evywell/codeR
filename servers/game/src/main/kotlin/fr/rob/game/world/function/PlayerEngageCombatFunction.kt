package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.CombatProto.PlayerEngageCombat
import fr.rob.game.combat.CombatTrait
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.player.session.GameSession

class PlayerEngageCombatFunction : WorldFunctionInterface {
    override fun invoke(
        sender: GameSession,
        opcode: Int,
        message: Message,
    ) {
        message as PlayerEngageCombat

        val controlledWorldObject = sender.controlledWorldObject ?: return
        val target = controlledWorldObject.mapInstance.findObjectByGuid(ObjectGuid(message.target))

        if (target.isEmpty) {
            return
        }

        controlledWorldObject.getTrait<CombatTrait>().ifPresent {
            it.engageCombatWith(target.get())
        }
    }

    override fun parseFromByteString(data: ByteString): Message = PlayerEngageCombat.parseFrom(data)
}

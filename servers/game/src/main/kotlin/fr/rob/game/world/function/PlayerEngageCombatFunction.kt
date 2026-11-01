package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.CombatProto.PlayerEngageCombat
import fr.rob.game.behavior.CombatBehavior
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.WorldObject
import fr.rob.game.player.session.GameSession

class PlayerEngageCombatFunction : InWorldFunctionInterface {
    override fun invoke(sender: GameSession, controlled: WorldObject, message: Message) {
        message as PlayerEngageCombat

        val target = controlled.mapInstance.findObjectByGuid(ObjectGuid(message.target)) ?: return

        CombatBehavior.engage(controlled, target)
    }

    override fun parseFromByteString(data: ByteString): Message = PlayerEngageCombat.parseFrom(data)
}

package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.SellProto.CastSpell
import fr.rob.game.entity.WorldObject
import fr.rob.game.player.session.GameSession
import fr.rob.game.spell.SpellCasterTrait
import fr.rob.game.spell.target.SpellTargetParameter

class CastSpellFunction : InWorldFunctionInterface {
    override fun invoke(sender: GameSession, controlled: WorldObject, message: Message) {
        message as CastSpell

        controlled.getTrait<SpellCasterTrait>().ifPresent { casterTrait ->
            casterTrait.castSpell(message.spellId, SpellTargetParameter(null, controlled.mapInstance))
        }
    }

    override fun parseFromByteString(data: ByteString): Message = CastSpell.parseFrom(data)
}

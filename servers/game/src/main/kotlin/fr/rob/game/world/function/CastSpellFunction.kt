package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.SellProto.CastSpell
import fr.rob.game.player.session.GameSession
import fr.rob.game.spell.SpellCasterTrait
import fr.rob.game.spell.target.SpellTargetParameter

class CastSpellFunction : WorldFunctionInterface {
    override fun invoke(
        sender: GameSession,
        opcode: Int,
        message: Message,
    ) {
        message as CastSpell

        val controlledWorldObject = sender.controlledWorldObject ?: return

        controlledWorldObject.getTrait<SpellCasterTrait>().ifPresent { casterTrait ->
            casterTrait.castSpell(message.spellId, SpellTargetParameter(null, controlledWorldObject.mapInstance))
        }
    }

    override fun parseFromByteString(data: ByteString): Message = CastSpell.parseFrom(data)
}

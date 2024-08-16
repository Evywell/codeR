package fr.rob.game.test.feature.specs

import fr.raven.proto.message.game.CombatProto.PlayerEngageCombat
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import org.junit.jupiter.api.Test

class PlayerEngageCombat {
    @Test
    fun `As player, I should be able to attack a unit`() {
        val gen = ObjectGuidGenerator()
        val guid = gen.createForPlayer(23)

        val built = PlayerEngageCombat.newBuilder()
            .setTarget(guid.getRawValue())
            .build()

        val low = built.target and 0xFFFFF
        val high = built.target shr 36

        val newGuid = ObjectGuid(low.toUInt(), high.toInt())

        println("raw ${guid.getRawValue()}")
        println("low $low")
        println("high $high")
        println("new raw ${newGuid.getRawValue()}")
    }
}

package fr.rob.game.test.unit.tools

import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.player.Player
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.test.unit.sandbox.network.session.NullMessageSender
import fr.rob.game.domain.entity.Unit as EntityUnit

class DummyWorldObjectBuilder(
    private val objectGuidGenerator: ObjectGuidGenerator,
) {
    private var entry: UInt = 1u

    fun createPlayer(): Player =
        Player(
            GameSession(1, NullMessageSender()),
            objectGuidGenerator.createForPlayer(1),
            "Test",
            1,
        )

    fun createUnit(): EntityUnit {
        val entry = getEntry()

        return EntityUnit(
            objectGuidGenerator.fromGuidInfo(
                ObjectGuidGenerator.GuidInfo(
                    ObjectGuid.LowGuid(entry, 0u),
                    ObjectGuid.GUID_TYPE.GAME_OBJECT,
                ),
            ),
            "Unit $entry",
            1,
        )
    }

    private fun getEntry(): UInt = entry++
}

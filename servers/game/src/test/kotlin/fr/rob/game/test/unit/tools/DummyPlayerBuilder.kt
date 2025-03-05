package fr.rob.game.test.unit.tools

import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.player.Player
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.test.unit.sandbox.network.session.NullMessageSender

class DummyPlayerBuilder(private val objectGuidGenerator: ObjectGuidGenerator) {
    fun createPlayer(): Player =
        Player(
            GameSession(1, NullMessageSender()),
            objectGuidGenerator.createForPlayer(1),
            "Test",
            1
        )
}

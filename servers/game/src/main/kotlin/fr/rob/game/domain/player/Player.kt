package fr.rob.game.domain.player

import fr.rob.game.domain.entity.Unit
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.player.session.GameSession

class Player(
    val ownerGameSession: GameSession,
    guid: ObjectGuid,
    name: String,
    level: Int
) : Unit(guid, name, level) {

    init {
        controlledByGameSession = ownerGameSession
    }
}

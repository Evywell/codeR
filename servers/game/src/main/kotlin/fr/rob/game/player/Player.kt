package fr.rob.game.player

import fr.rob.game.entity.Unit
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.player.session.GameSession

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

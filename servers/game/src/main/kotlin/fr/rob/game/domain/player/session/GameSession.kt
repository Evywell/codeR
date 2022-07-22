package fr.rob.game.domain.player.session

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.player.Player

class GameSession(val accountId: Int, private val messageSender: SessionMessageSenderInterface) {

    var loggedAsPlayer: Player? = null
    var controlledWorldObject: WorldObject? = null

    fun assignToPlayer(player: Player) {
        loggedAsPlayer = player
        controlledWorldObject = player
    }

    fun send(message: GameMessageInterface) {
        messageSender.send(this, message.createGameMessageHolder())
    }
}

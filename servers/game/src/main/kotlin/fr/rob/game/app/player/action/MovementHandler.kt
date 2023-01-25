package fr.rob.game.app.player.action

import fr.rob.game.domain.entity.behavior.MovingBehavior

class MovementHandler {
    fun execute(command: MovementCommand) {
        val player = command.player
        player.setMoving(MovingBehavior(player), command.movementInfo)
    }
}

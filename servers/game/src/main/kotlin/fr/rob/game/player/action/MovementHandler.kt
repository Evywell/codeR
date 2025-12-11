package fr.rob.game.player.action

import fr.rob.game.entity.Movement
import fr.rob.game.entity.behavior.MovableTrait

class MovementHandler {
    fun execute(command: MovementCommand) {
        val worldObject = command.session.controlledWorldObject

        if (worldObject === null || !worldObject.guid.isPlayer()) {
            return
        }

        val movingBehavior = worldObject.getTrait<MovableTrait>()

        if (movingBehavior.isEmpty) {
            return
        }

        movingBehavior.get().move(Movement(command.movementDirectionType, command.orientation))
    }
}

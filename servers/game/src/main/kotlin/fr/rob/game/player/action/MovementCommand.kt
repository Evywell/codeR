package fr.rob.game.player.action

import fr.rob.game.entity.Movement
import fr.rob.game.player.session.GameSession

data class MovementCommand(
    val session: GameSession,
    val movementDirectionType: Movement.MovementDirectionType,
    val orientation: Float
)

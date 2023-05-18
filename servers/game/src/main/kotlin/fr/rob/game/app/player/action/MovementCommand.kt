package fr.rob.game.app.player.action

import fr.rob.game.domain.entity.Movement
import fr.rob.game.domain.player.session.GameSession

data class MovementCommand(
    val session: GameSession,
    val movementDirectionType: Movement.MovementDirectionType,
    val orientation: Float
)

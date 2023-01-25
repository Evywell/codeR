package fr.rob.game.app.player.action

import fr.rob.game.domain.entity.Movement
import fr.rob.game.domain.player.Player

data class MovementCommand(val player: Player, val movementInfo: Movement)

package fr.rob.game.player.action

import fr.rob.game.instance.MapInstance
import fr.rob.game.player.session.GameSession

data class CreatePlayerIntoWorldCommand(val gameSession: GameSession, val characterId: Int, val mapInstance: MapInstance)

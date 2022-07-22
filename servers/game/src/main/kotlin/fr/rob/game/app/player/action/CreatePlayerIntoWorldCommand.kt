package fr.rob.game.app.player.action

import fr.rob.game.domain.player.session.GameSession

data class CreatePlayerIntoWorldCommand(val gameSession: GameSession, val characterId: Int)

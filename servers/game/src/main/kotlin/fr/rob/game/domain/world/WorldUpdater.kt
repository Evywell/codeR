package fr.rob.game.domain.world

import fr.rob.game.domain.player.session.GameSessionUpdaterInterface

class WorldUpdater(private val gameSessionUpdater: GameSessionUpdaterInterface) {
    fun update(deltaTime: Long) {
        gameSessionUpdater.updateSession(deltaTime)
    }
}

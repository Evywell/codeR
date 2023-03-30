package fr.rob.game.domain.world

data class WorldUpdateState(var timeElapsedSinceLastUpdate: Int = 0, var timeElapsedSinceStartup: Long = 0)

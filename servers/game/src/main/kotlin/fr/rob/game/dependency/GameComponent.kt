package fr.rob.game.dependency

import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.game.DB_WORLD
import fr.rob.game.config.GameConfig
import fr.rob.game.game.world.map.MapManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameComponent(private val config: GameConfig) : KoinComponent {
    val mapManager: MapManager by inject()
    val loggerFactory: LoggerFactoryInterface by inject()

    private val connectionPoolManager: ConnectionPoolManager by inject()

    fun initWorldDatabase() {
        connectionPoolManager.createPool(DB_WORLD, config.databases.worldDb)
    }
}

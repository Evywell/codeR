package fr.rob.game.config.database

import fr.rob.core.config.Config
import fr.rob.core.config.database.AbstractDatabaseConfigHandler
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.game.DATABASE
import fr.rob.game.DB_CONFIG
import fr.rob.game.DB_WORLD

class DatabaseConfigHandler(private val connectionPoolManager: ConnectionPoolManager) :
    AbstractDatabaseConfigHandler() {

    override fun getConfigKey(): String = DATABASE

    override fun handle(config: Config): Any {
        // Config database
        loadConfigDatabase(config)

        // World database
        loadWorldDatabase(config)

        // Load other databases (players, ...)

        return connectionPoolManager
    }

    private fun loadConfigDatabase(config: Config) {
        if (config.getString(getDatabaseKey("config.database"), null) == null) {
            return
        }

        connectionPoolManager.createPool(DB_CONFIG, getDatabaseConfig(config, "config"))
    }

    private fun loadWorldDatabase(config: Config) {
        if (config.getString(getDatabaseKey("world.database"), null) == null) {
            return
        }

        connectionPoolManager.createPool(DB_WORLD, getDatabaseConfig(config, "world"))
    }
}

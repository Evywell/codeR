package fr.rob.game.infrastructure.config.database

import fr.rob.core.config.Config
import fr.rob.core.config.database.AbstractDatabaseConfigHandler
import fr.rob.core.database.ConnectionManager
import fr.rob.game.DATABASE
import fr.rob.game.DB_CONFIG

class DatabaseConfigHandler(private val connectionManager: ConnectionManager) : AbstractDatabaseConfigHandler() {

    override fun getConfigKey(): String = DATABASE

    override fun handle(config: Config): Any {
        // Config database
        loadConfigDatabase(config)

        // Load other databases (players, world, ...)

        return connectionManager
    }

    private fun loadConfigDatabase(config: Config) {
        if (config.getString(getDatabaseKey("config.database"), null) == null) {
            return
        }

        connectionManager.newConnection(DB_CONFIG, getDatabaseConfig(config, "config"))
    }
}

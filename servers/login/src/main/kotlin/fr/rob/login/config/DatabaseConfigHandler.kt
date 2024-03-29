package fr.rob.login.config

import fr.rob.core.DB_CONFIG
import fr.rob.core.config.Config
import fr.rob.core.config.database.AbstractDatabaseConfigHandler
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.login.CONFIG_KEY_DATABASES
import fr.rob.login.DB_PLAYERS

class DatabaseConfigHandler(private val connectionPoolManager: ConnectionPoolManager) :
    AbstractDatabaseConfigHandler() {

    override fun getConfigKey(): String = CONFIG_KEY_DATABASES

    override fun handle(config: Config): Any {
        loadPlayersDatabase(config)
        loadConfigDatabase(config)

        return connectionPoolManager
    }

    private fun loadPlayersDatabase(config: Config) {
        if (config.getString(getDatabaseKey("players.database"), null) == null) {
            return
        }

        connectionPoolManager.createPool(DB_PLAYERS, getDatabaseConfig(config, "players"))
    }

    private fun loadConfigDatabase(config: Config) {
        if (config.getString(getDatabaseKey("config.database"), null) == null) {
            return
        }

        connectionPoolManager.createPool(DB_CONFIG, getDatabaseConfig(config, "config"))
    }
}

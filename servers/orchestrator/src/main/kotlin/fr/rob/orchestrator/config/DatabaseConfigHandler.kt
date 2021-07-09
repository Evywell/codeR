package fr.rob.orchestrator.config

import fr.rob.core.config.Config
import fr.rob.core.config.database.AbstractDatabaseConfigHandler
import fr.rob.core.database.ConnectionManager

class DatabaseConfigHandler(private val connectionManager: ConnectionManager) : AbstractDatabaseConfigHandler() {

    override fun getConfigKey(): String = CONFIG_KEY_DATABASES

    override fun handle(config: Config): Any {
        loadConfigDatabase(config)

        return connectionManager
    }

    private fun loadConfigDatabase(config: Config) {
        if (config.getString(getDatabaseKey("config.database"), null) == null) {
            return
        }

        connectionManager.newConnection(DB_CONFIG, getDatabaseConfig(config, DB_CONFIG))
    }

    companion object {
        const val CONFIG_KEY_DATABASES = "databases"

        const val DB_CONFIG = "config"
    }
}

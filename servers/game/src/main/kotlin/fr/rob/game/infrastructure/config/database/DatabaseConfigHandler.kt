package fr.rob.game.infrastructure.config.database

import fr.rob.core.config.Config
import fr.rob.core.config.ConfigHandlerInterface
import fr.rob.game.DATABASE
import fr.rob.game.DB_CONFIG
import fr.rob.game.infrastructure.database.ConnectionManager

class DatabaseConfigHandler(private val connectionManager: ConnectionManager) : ConfigHandlerInterface {

    override fun getConfigKey(): String = DATABASE

    override fun handle(config: Config): Any {
        // Config database
        loadConfigDatabase(config)

        // Load other databases (characters, world, ...)

        return connectionManager
    }

    private fun loadConfigDatabase(config: Config) {
        if (config.getString(getDatabaseKey("config.database"), null) == null) {
            return
        }

        connectionManager.newConnection(DB_CONFIG, getDatabaseConfig(config, "config"))
    }

    private fun getDatabaseConfig(config: Config, prefixKey: String): DatabaseConfig =
         DatabaseConfig(
            config.getString(getDatabaseKey("$prefixKey.host"), "localhost")!!,
            config.getLong(getDatabaseKey("$prefixKey.port"), 3306)!!,
            config.getString(getDatabaseKey("$prefixKey.user"))!!,
            config.getString(getDatabaseKey("$prefixKey.password"))!!,
            config.getString(getDatabaseKey("$prefixKey.database"))!!
        )


    private fun getDatabaseKey(key: String): String = getConfigKey() + '.' + key
}

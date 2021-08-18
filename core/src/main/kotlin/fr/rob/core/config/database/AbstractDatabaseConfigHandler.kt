package fr.rob.core.config.database

import fr.rob.core.config.Config
import fr.rob.core.config.ConfigHandlerInterface

abstract class AbstractDatabaseConfigHandler : ConfigHandlerInterface {

    protected fun getDatabaseConfig(config: Config, prefixKey: String): DatabaseConfig =
        DatabaseConfig(
            config.getString(getDatabaseKey("$prefixKey.host"), "localhost")!!,
            config.getLong(getDatabaseKey("$prefixKey.port"), 3306)!!,
            config.getString(getDatabaseKey("$prefixKey.user"))!!,
            config.getString(getDatabaseKey("$prefixKey.password"))!!,
            config.getString(getDatabaseKey("$prefixKey.database"))!!
        )

    protected fun getDatabaseKey(key: String): String = getConfigKey() + '.' + key
}

package fr.rob.login.config

import fr.rob.core.config.Config
import fr.rob.core.config.database.AbstractDatabaseConfigHandler
import fr.rob.login.CONFIG_KEY_DATABASES

class DatabaseConfigHandler : AbstractDatabaseConfigHandler() {

    override fun getConfigKey(): String = CONFIG_KEY_DATABASES

    override fun handle(config: Config): Any {
        TODO("Not yet implemented")
    }
}

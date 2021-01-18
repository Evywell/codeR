package fr.rob.game.infrastructure.config

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.game.*

class ConfigModule(private val app: BaseApplication) : AbstractModule() {

    override fun boot() {
        app.env = retrieveEnv()
    }

    /**
     * Retrieves the right application environment using global ENV variables and config file
     */
    private fun retrieveEnv(): String {
        val config = app.getConfig(CONFIG_DEFAULT)

        val environmentVariableEnv = System.getenv(APP_ENV_KEY)

        if (environmentVariableEnv !== null && isEnvValid(environmentVariableEnv)) {
            return environmentVariableEnv
        }

        val configEnv = config.get("env") as String?

        if (configEnv !== null && isEnvValid(configEnv)) {
            return configEnv
        }

        return ENV_DEV
    }

    private fun isEnvValid(envName: String): Boolean = listOf(ENV_DEV, ENV_PROD, ENV_TEST).contains(envName)
}

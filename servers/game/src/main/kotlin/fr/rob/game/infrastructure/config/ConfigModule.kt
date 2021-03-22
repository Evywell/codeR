package fr.rob.game.infrastructure.config

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.game.*

class ConfigModule(private val app: Main) : AbstractModule() {

    override fun boot() {
        app.env = retrieveEnv()
        app.config.retrieveConfig(DATABASE) // Load the database configurations
    }

    /**
     * Retrieves the right application environment using global ENV variables and config file
     */
    private fun retrieveEnv(): String {
        val environmentVariableEnv = System.getenv(APP_ENV_KEY)

        if (environmentVariableEnv !== null && isEnvValid(environmentVariableEnv)) {
            return environmentVariableEnv
        }

        val config = app.config.retrieveConfig("env") as String

        if (isEnvValid(config)) {
            return config
        }

        return ENV_DEV
    }

    private fun isEnvValid(envName: String): Boolean = listOf(ENV_DEV, ENV_PROD, ENV_TEST).contains(envName)
}

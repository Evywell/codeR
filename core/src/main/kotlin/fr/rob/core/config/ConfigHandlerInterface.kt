package fr.rob.core.config

interface ConfigHandlerInterface {

    /**
     * Returns the name of the configuration. It is used by the Config to retrieve the correct handler
     */
    fun getConfigKey(): String

    /**
     * Uses the Config to read values from the configuration file and creates a custom config object
     *
     * @example Create a simple database configuration
     * ```kotlin
     * val dsn = config.getString("dsn")
     *
     * return DatabaseConfig(dsn)
     * ````
     */
    fun handle(config: Config): Any?
}

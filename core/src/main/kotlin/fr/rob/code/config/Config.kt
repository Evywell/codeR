package fr.rob.code.config

import java.util.HashMap

abstract class Config {

    private val cachedConfigResult = HashMap<String, Any?>()
    private val handlers = ArrayList<ConfigHandlerInterface>()

    abstract fun getString(configurationKey: String, default: String? = ""): String?
    abstract fun getBoolean(configurationKey: String, default: Boolean? = false): Boolean?
    abstract fun getInteger(configurationKey: String, default: Int? = 0): Int?
    abstract fun getLong(configurationKey: String, default: Long? = 0): Long?
    abstract fun getFloat(configurationKey: String, default: Float? = 0f): Float?
    abstract fun getDouble(configurationKey: String, default: Double? = 0.0): Double?
    abstract fun getByte(configurationKey: String, default: Byte? = 0): Byte?
    abstract fun getStringArray(configurationKey: String): Array<String>?
    abstract fun get(configurationKey: String): Any?

    fun addHandler(handler: ConfigHandlerInterface): Config {
        handlers.add(handler)

        return this
    }

    fun retrieveConfig(configKey: String): Any? {
        if (!cachedConfigResult.containsKey(configKey)) {
            val handler = getHandlerForKey(configKey)

            cachedConfigResult[configKey] = handler.handle(this)
        }

        return cachedConfigResult[configKey]
    }

    private fun getHandlerForKey(configKey: String): ConfigHandlerInterface {
        for (handler in handlers) {
            if (handler.getConfigKey() == configKey) {
                return handler
            }
        }

        throw ConfigHandlerNotFoundException("There is no handler for the specified key $configKey")
    }
}

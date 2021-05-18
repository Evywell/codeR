package fr.rob.login.test.feature.service.config

import fr.rob.core.config.Config

class Config : Config() {

    val properties = HashMap<String, Any>()

    override fun getString(configurationKey: String, default: String?): String? =
        properties[configurationKey]?.toString()

    override fun getBoolean(configurationKey: String, default: Boolean?): Boolean? =
        properties[configurationKey] as Boolean?

    override fun getInteger(configurationKey: String, default: Int?): Int? = properties[configurationKey] as Int?

    override fun getLong(configurationKey: String, default: Long?): Long? = properties[configurationKey] as Long?

    override fun getFloat(configurationKey: String, default: Float?): Float? = properties[configurationKey] as Float?

    override fun getDouble(configurationKey: String, default: Double?): Double? =
        properties[configurationKey] as Double?

    override fun getByte(configurationKey: String, default: Byte?): Byte? = properties[configurationKey] as Byte?

    override fun getStringArray(configurationKey: String): Array<String>? = null

    override fun get(configurationKey: String): Any? = null
}

package fr.rob.login.test.feature.service.config

import fr.rob.core.config.Config

class Config : Config() {

    override fun getString(configurationKey: String, default: String?): String? = null

    override fun getBoolean(configurationKey: String, default: Boolean?): Boolean? = null

    override fun getInteger(configurationKey: String, default: Int?): Int? = null

    override fun getLong(configurationKey: String, default: Long?): Long? = null

    override fun getFloat(configurationKey: String, default: Float?): Float? = null

    override fun getDouble(configurationKey: String, default: Double?): Double? = null

    override fun getByte(configurationKey: String, default: Byte?): Byte? = null

    override fun getStringArray(configurationKey: String): Array<String>? = null

    override fun get(configurationKey: String): Any? = null
}

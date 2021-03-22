package fr.rob.code.config.commons.configuration2

import fr.rob.code.config.Config
import org.apache.commons.configuration2.FileBasedConfiguration
import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters
import java.io.File

class Config(file: File) : Config() {

    val config: FileBasedConfiguration

    init {
        val params = Parameters()
        val properties = params.properties()

        properties.setFile(file)

        val builder = FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration::class.java)

        builder
            .configure(properties)

        config = builder.configuration
    }

    override fun getString(configurationKey: String, default: String?): String? = config.getString(configurationKey, default)

    override fun getBoolean(configurationKey: String, default: Boolean?): Boolean? = config.getBoolean(configurationKey, default)

    override fun getInteger(configurationKey: String, default: Int?): Int? = config.getInteger(configurationKey, default)

    override fun getLong(configurationKey: String, default: Long?): Long? = config.getLong(configurationKey, default)

    override fun getFloat(configurationKey: String, default: Float?): Float? = config.getFloat(configurationKey, default)

    override fun getDouble(configurationKey: String, default: Double?): Double? = config.getDouble(configurationKey, default)

    override fun getByte(configurationKey: String, default: Byte?): Byte? = config.getByte(configurationKey, default)

    override fun getStringArray(configurationKey: String): Array<String>? = config.getStringArray(configurationKey)

    override fun get(configurationKey: String): Any? = config.getProperty(configurationKey)

}

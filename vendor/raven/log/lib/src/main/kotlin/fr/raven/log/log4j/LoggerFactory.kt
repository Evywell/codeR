package fr.raven.log.log4j

import fr.raven.log.LoggerFactoryInterface
import fr.raven.log.LoggerInterface
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.appender.ConsoleAppender
import org.apache.logging.log4j.core.config.ConfigurationSource
import org.apache.logging.log4j.core.config.LoggerConfig
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory
import org.apache.logging.log4j.core.layout.PatternLayout
import java.io.File

class LoggerFactory(private val configurationFile: File) : LoggerFactoryInterface {

    override fun create(name: String): LoggerInterface {
        val configurationFactory = XmlConfigurationFactory.getInstance()

        val configurationSource = ConfigurationSource(configurationFile.inputStream())

        val loggerContext = LoggerContext("GlobalConfigContext")
        val configuration = configurationFactory.getConfiguration(loggerContext, configurationSource)

        val consoleAppender: ConsoleAppender =
            ConsoleAppender.createDefaultAppenderForLayout(PatternLayout.createDefaultLayout())

        configuration.addAppender(consoleAppender)

        val loggerConfig = LoggerConfig(name, Level.ALL, false)
        loggerConfig.addAppender(consoleAppender, null, null)

        configuration.addLogger(name, loggerConfig)

        loggerContext.start(configuration)

        return Logger(loggerContext.getLogger(name))
    }
}

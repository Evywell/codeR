package fr.rob.core.log

import fr.rob.core.misc.ResourceManager
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.appender.ConsoleAppender
import org.apache.logging.log4j.core.config.ConfigurationSource
import org.apache.logging.log4j.core.config.LoggerConfig
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory
import org.apache.logging.log4j.core.layout.PatternLayout

object LoggerFactory : LoggerFactoryInterface {

    private const val LOG4J_CONFIG_RESOURCE_PATH = "log4j.config.xml"

    override fun create(name: String): LoggerInterface {
        val configurationFactory = XmlConfigurationFactory.getInstance()

        val configurationSource = ConfigurationSource(
            ResourceManager.getResourceStream(LOG4J_CONFIG_RESOURCE_PATH)
        )

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

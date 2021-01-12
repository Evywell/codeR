package fr.rob.game.infrastructure.log

import fr.rob.game.LOG4J_CONFIG_RESOURCE_PATH
import fr.rob.game.domain.log.LoggerFactoryInterface
import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.infrastructure.config.ResourceManager
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.appender.ConsoleAppender
import org.apache.logging.log4j.core.config.ConfigurationSource
import org.apache.logging.log4j.core.config.LoggerConfig
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory
import org.apache.logging.log4j.core.layout.PatternLayout
import java.io.File
import java.io.FileInputStream

object LoggerFactory : LoggerFactoryInterface {

    override fun create(name: String): LoggerInterface {
        val configurationFactory = XmlConfigurationFactory.getInstance()

        val configurationSource = ConfigurationSource(
            FileInputStream(
                File(
                    ResourceManager.getResourceURL(LOG4J_CONFIG_RESOURCE_PATH)!!.toURI()
                )
            )
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

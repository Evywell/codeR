package fr.rob.login

import fr.raven.log.log4j.LoggerFactory
import fr.rob.core.ENV_DEV
import fr.rob.core.config.commons.configuration2.Config
import java.io.File

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val logConfigResource = requireNotNull({}.javaClass.classLoader.getResource("log4j.config.xml")) {
                "log4j.config.xml not found in classpath"
            }
            val loggerFactory = LoggerFactory(File(logConfigResource.path))
            val app = LoginApplication(env = ENV_DEV, loggerFactory = loggerFactory)
            app.config = Config(File("config.properties"))

            app.run()
        }
    }
}

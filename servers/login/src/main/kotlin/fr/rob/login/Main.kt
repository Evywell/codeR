package fr.rob.login

import fr.raven.log.log4j.LoggerFactory
import fr.rob.core.ENV_DEV
import fr.rob.core.config.commons.configuration2.Config
import java.io.File

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val loggerFactory = LoggerFactory(File({}.javaClass.classLoader.getResource("log4j.config.xml")!!.path))
            val app = LoginApplication(env = ENV_DEV, loggerFactory = loggerFactory)
            app.config = Config(File("config.properties"))

            app.run()
        }
    }
}

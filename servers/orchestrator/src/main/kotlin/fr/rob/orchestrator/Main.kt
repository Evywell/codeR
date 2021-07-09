package fr.rob.orchestrator

import fr.rob.core.ENV_DEV
import fr.rob.core.config.commons.configuration2.Config
import fr.rob.core.log.LoggerFactory
import java.io.File

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val app = OrchestratorApplication(LoggerFactory, ENV_DEV)
            app.config = Config(File("config.properties"))

            app.run()
        }
    }
}

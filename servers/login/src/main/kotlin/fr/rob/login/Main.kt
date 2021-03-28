package fr.rob.login

import fr.rob.core.ENV_DEV
import fr.rob.core.log.LoggerFactory

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val app = LoginApplication(env = ENV_DEV, loggerFactory = LoggerFactory)
            app.run()
        }
    }
}
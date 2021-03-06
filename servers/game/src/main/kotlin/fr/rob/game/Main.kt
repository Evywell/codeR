package fr.rob.game

import com.xenomachina.argparser.ArgParser
import fr.rob.core.config.Config
import fr.rob.core.ENV_DEV
import fr.rob.game.domain.args.GameServerArgs
import fr.rob.core.database.ConnectionManager
import fr.rob.core.event.EventManager
import java.io.File
import java.io.InputStream
import java.nio.file.Paths


class Main {

    lateinit var config: Config

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var configFileName: String? = null

            ArgParser(args).parseInto(::GameServerArgs).run {
                // configFileName = "$config"
            }

            val eventManager = EventManager()
            val connectionManager = ConnectionManager(eventManager)

            val app = GameServerSupervisorApplication(ENV_DEV, eventManager, connectionManager)
            app.config = app.loadConfig(getConfigFile(configFileName))

            app.run()
        }

        private fun getConfigFile(configFileName: String?): File {
            if (configFileName != null) {
                return File(configFileName)
            }

            val stream: InputStream = Main::class.java.classLoader.getResourceAsStream(CONFIG_FILE)!!

            val tmpConfig = File.createTempFile("config", ".tmp", File(Paths.get("").toAbsolutePath().toString()))
            tmpConfig.writeBytes(stream.readAllBytes())

            tmpConfig.deleteOnExit()

            return tmpConfig
        }
    }
}

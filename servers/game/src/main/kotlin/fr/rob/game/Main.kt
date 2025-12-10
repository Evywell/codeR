package fr.rob.game

import fr.rob.core.config.Config
import fr.rob.core.config.commons.configuration2.ConfigLoader
import fr.rob.core.config.database.DatabaseConfig
import fr.rob.game.infra.App
import fr.rob.game.infra.config.Databases
import fr.rob.game.infra.config.GameConfig
import fr.rob.game.infra.config.NodesConfig
import fr.rob.game.infra.config.Orchestrator
import fr.rob.game.infra.dependency.databaseModule
import fr.rob.game.infra.dependency.globalModule
import fr.rob.game.infra.dependency.mapModule
import fr.rob.game.infra.dependency.opcodeModule
import org.koin.core.context.startKoin
import java.io.File
import java.io.InputStream
import java.nio.file.Paths

class Main {

    lateinit var config: Config

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val configLoader = ConfigLoader()
            val globalConfig = configLoader.loadConfigFromFile(getConfigFile("config.properties"))
            val config = fromGlobal(globalConfig)

            startKoin {
                modules(globalModule, databaseModule, mapModule, opcodeModule)
            }

            val app = App(config)
            app.run()
        }

        private fun fromGlobal(config: Config): GameConfig = GameConfig(
            Orchestrator(
                requireNotNull(config.getString("orchestrator.host")) { "Missing config: orchestrator.host" },
                requireNotNull(config.getInteger("orchestrator.port")) { "Missing config: orchestrator.port" },
            ),
            Databases(
                DatabaseConfig(
                    System.getProperty("mysql_game.host"),
                    System.getProperty("mysql_game.tcp.3306").toLong(),
                    requireNotNull(config.getString("databases.config.user")) { "Missing config: databases.config.user" },
                    requireNotNull(config.getString("databases.config.password")) { "Missing config: databases.config.password" },
                    requireNotNull(config.getString("databases.config.database")) { "Missing config: databases.config.database" },
                ),
                DatabaseConfig(
                    System.getProperty("mysql_game.host"),
                    System.getProperty("mysql_game.tcp.3306").toLong(),
                    requireNotNull(config.getString("databases.players.user")) { "Missing config: databases.players.user" },
                    requireNotNull(config.getString("databases.players.password")) { "Missing config: databases.players.password" },
                    requireNotNull(config.getString("databases.players.database")) { "Missing config: databases.players.database" },
                ),
                DatabaseConfig(
                    System.getProperty("mysql_game.host"),
                    System.getProperty("mysql_game.tcp.3306").toLong(),
                    requireNotNull(config.getString("databases.world.user")) { "Missing config: databases.world.user" },
                    requireNotNull(config.getString("databases.world.password")) { "Missing config: databases.world.password" },
                    requireNotNull(config.getString("databases.world.database")) { "Missing config: databases.world.database" },
                ),
            ),
            NodesConfig(config),
        )

        private fun getConfigFile(configFileName: String?): File {
            if (configFileName != null) {
                return File(configFileName)
            }

            val stream: InputStream = requireNotNull(Main::class.java.classLoader.getResourceAsStream(CONFIG_FILE)) {
                "Config file not found: $CONFIG_FILE"
            }

            val tmpConfig = File.createTempFile("config", ".tmp", File(Paths.get("").toAbsolutePath().toString()))
            tmpConfig.writeBytes(stream.readAllBytes())

            tmpConfig.deleteOnExit()

            return tmpConfig
        }
    }
}

package fr.rob.game

import fr.raven.messaging.rabbitmq.AMQPConfig
import fr.rob.core.config.Config
import fr.rob.core.config.commons.configuration2.ConfigLoader
import fr.rob.core.config.database.DatabaseConfig
import fr.rob.game.config.Databases
import fr.rob.game.config.GameConfig
import fr.rob.game.config.NodesConfig
import fr.rob.game.config.Orchestrator
import fr.rob.game.dependency.databaseModule
import fr.rob.game.dependency.globalModule
import fr.rob.game.dependency.mapModule
import fr.rob.game.dependency.queueModule
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
                modules(globalModule, databaseModule, mapModule, queueModule)
            }

            val app = App(config)
            app.run()
        }

        private fun fromGlobal(config: Config): GameConfig = GameConfig(
            Orchestrator(
                config.getString("orchestrator.host")!!,
                config.getInteger("orchestrator.port")!!
            ),
            Databases(
                DatabaseConfig(
                    System.getProperty("mysql_game.host"),
                    System.getProperty("mysql_game.tcp.3306").toLong(),
                    config.getString("databases.config.user")!!,
                    config.getString("databases.config.password")!!,
                    config.getString("databases.config.database")!!
                ),
                DatabaseConfig(
                    System.getProperty("mysql_game.host"),
                    System.getProperty("mysql_game.tcp.3306").toLong(),
                    config.getString("databases.players.user")!!,
                    config.getString("databases.players.password")!!,
                    config.getString("databases.players.database")!!
                ),
                DatabaseConfig(
                    System.getProperty("mysql_game.host"),
                    System.getProperty("mysql_game.tcp.3306").toLong(),
                    config.getString("databases.world.user")!!,
                    config.getString("databases.world.password")!!,
                    config.getString("databases.world.database")!!
                )
            ),
            NodesConfig(config),
            AMQPConfig(
                System.getProperty("rabbit.host"),
                System.getProperty("rabbit.tcp.5672").toInt(),
                config.getString("rabbitmq.username")!!,
                config.getString("rabbitmq.password")!!,
                config.getString("rabbitmq.vhost")!!
            )
        )

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

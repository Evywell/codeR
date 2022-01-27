package fr.rob.game

import fr.rob.core.config.Config
import fr.rob.core.config.commons.configuration2.ConfigLoader
import fr.rob.core.config.database.DatabaseConfig
import fr.rob.game.config.Databases
import fr.rob.game.config.GameConfig
import fr.rob.game.config.NodesConfig
import fr.rob.game.config.Orchestrator
import fr.rob.game.dependency.GameComponent
import fr.rob.game.dependency.databaseModule
import fr.rob.game.dependency.globalModule
import fr.rob.game.dependency.mapModule
import fr.rob.game.network.node.GameNodeManager
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

            startKoin { modules(globalModule, databaseModule, mapModule) }

            val component = GameComponent(config)
            component.initWorldDatabase()

            val nodeManager = GameNodeManager(config.nodesConfig.maxNodes, component.loggerFactory)
            val nodeAgentAdapter = NodeAgentAdapter(nodeManager, component.mapManager)

            val app = App(config, nodeManager, component.mapManager, nodeAgentAdapter)
            app.run()
/*
            var configFileName: String? = null

            ArgParser(args).parseInto(::GameServerArgs).run {
                // configFileName = "$config"
            }



            val mapManager = MapManager(component.mapLoader, component.creatureLoader)

            val app = GameServerSupervisorApplication(
                config,
                ENV_DEV,
                LoggerFactory,
                component.eventManager,
                component.connectionPoolManager,
                mapManager
            )
            app.config = app.loadConfig(getConfigFile(configFileName))

            app.run()
            */
        }

        private fun fromGlobal(config: Config): GameConfig = GameConfig(
            Orchestrator(
                config.getString("orchestrator.host")!!,
                config.getInteger("orchestrator.port")!!
            ),
            Databases(
                DatabaseConfig(
                    config.getString("databases.config.host")!!,
                    config.getLong("databases.config.port")!!,
                    config.getString("databases.config.user")!!,
                    config.getString("databases.config.password")!!,
                    config.getString("databases.config.database")!!
                ),
                DatabaseConfig(
                    config.getString("databases.players.host")!!,
                    config.getLong("databases.players.port")!!,
                    config.getString("databases.players.user")!!,
                    config.getString("databases.players.password")!!,
                    config.getString("databases.players.database")!!
                ),
                DatabaseConfig(
                    config.getString("databases.world.host")!!,
                    config.getLong("databases.world.port")!!,
                    config.getString("databases.world.user")!!,
                    config.getString("databases.world.password")!!,
                    config.getString("databases.world.database")!!
                )
            ),
            NodesConfig(config)
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

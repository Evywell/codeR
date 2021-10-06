package fr.rob.login.test.cucumber.context

import fr.rob.core.config.hashmap.HashMapConfig
import fr.rob.core.log.LoggerFactory
import fr.rob.core.test.unit.sandbox.log.NILoggerFactory
import fr.rob.orchestrator.test.cucumber.service.OrchestratorNode

class OrchestratorContext {

    init {
        startInstance()
    }

    companion object {
        private var instance: OrchestratorNode? = null

        fun startInstance() {
            if (instance != null) {
                return
            }

            val config = HashMapConfig()

            config.properties["databases.config.host"] = "mysql_game"
            config.properties["databases.config.port"] = 3306L
            config.properties["databases.config.user"] = "testing"
            config.properties["databases.config.password"] = "passwordtesting"
            config.properties["databases.config.database"] = "config"

            config.properties["databases.players.host"] = "mysql_game"
            config.properties["databases.players.port"] = 3306L
            config.properties["databases.players.user"] = "testing"
            config.properties["databases.players.password"] = "passwordtesting"
            config.properties["databases.players.database"] = "players"

            config.properties["orchestrator.id"] = 1

            // Creating the orchestrator
            val orchestratorNodeInstance = OrchestratorNode(LoggerFactory, config)
            orchestratorNodeInstance.start() // Thread blocking while the server is not fully started

            instance = orchestratorNodeInstance
        }
    }
}

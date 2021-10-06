package fr.rob.orchestrator.test.cucumber.context

import fr.rob.core.config.Config
import fr.rob.core.config.hashmap.HashMapConfig
import fr.rob.core.database.Connection
import fr.rob.core.log.LoggerFactory
import fr.rob.core.test.unit.sandbox.log.NILogger
import fr.rob.orchestrator.ORCHESTRATOR_SERVER_PORT
import fr.rob.orchestrator.agent.AgentFactory
import fr.rob.orchestrator.agent.NodeAgent
import fr.rob.orchestrator.test.cucumber.service.OrchestratorNode

class OrchestratorContext {

    private var orchestratorNodeInstance: OrchestratorNode
    private var configDb: Connection
    private val agentFactory: AgentFactory

    var token: String
    var agents = HashMap<String, NodeAgent>()

    init {
        val config = createConfig()

        // Creating the orchestrator
        orchestratorNodeInstance = OrchestratorNode(LoggerFactory, config)
        orchestratorNodeInstance.start() // Thread blocking while the server is not fully started

        agentFactory = AgentFactory(orchestratorNodeInstance.app.orchestrator, NILogger())
        configDb = orchestratorNodeInstance.app.connectionManager.getConnection("config")!!

        val stmt = configDb.createPreparedStatement("SELECT token FROM orchestrators WHERE id = ?")!!
        stmt.setInt(1, config.getInteger("orchestrator.id")!!)
        stmt.execute()

        val rs = stmt.resultSet
        rs.next()

        token = rs.getString(1)
    }

    fun createAgent(): NodeAgent {
        return agentFactory.createNodeAgent("127.0.0.1", ORCHESTRATOR_SERVER_PORT)
    }

    fun close() {
        orchestratorNodeInstance.stop()
    }

    private fun createConfig(): Config {
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

        return config
    }
}

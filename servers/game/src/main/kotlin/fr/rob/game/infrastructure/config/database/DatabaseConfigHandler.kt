package fr.rob.game.infrastructure.config.database

import fr.rob.core.config.ConfigHandlerInterface
import fr.rob.core.database.DatabaseConfig
import fr.rob.game.DATABASE
import fr.rob.game.infrastructure.database.ConnectionManager
import org.codehaus.jackson.node.ObjectNode

class DatabaseConfigHandler(private val connectionManager: ConnectionManager) : ConfigHandlerInterface {

    override fun handle(node: ObjectNode): ConnectionManager {
        if (!node.has("databases")) {
            return connectionManager
        }

        val databasesNode = node["databases"]
        val databasesNodeFieldNames = databasesNode.fieldNames

        while (databasesNodeFieldNames.hasNext()) {
            val connectionName = databasesNodeFieldNames.next()
            val databaseNode = databasesNode.findValue(connectionName)
            val databaseConfig = DatabaseConfig(
                databaseNode["host"].textValue,
                databaseNode["port"].longValue,
                databaseNode["user"].textValue,
                databaseNode["password"].textValue,
                databaseNode["database"].textValue
            )

            connectionManager.newConnection(connectionName, databaseConfig)
        }

        return connectionManager
    }

    override fun getRootName() = "databases"

    override fun getName() = DATABASE
}

package fr.rob.core.database

import fr.rob.core.config.database.DatabaseConfig
import fr.rob.core.event.EventManagerInterface

class ConnectionManager(private val eventManager: EventManagerInterface) {

    private val connections: MutableMap<String, Connection> = HashMap()

    fun getConnection(connectionName: String): Connection {
        if (!connections.containsKey(connectionName)) {
            throw RuntimeException("The connection name `$connectionName` is not defined")
        }

        return connections[connectionName]!!
    }

    fun newConnection(connectionName: String, databaseConfig: DatabaseConfig): Connection {
        val connection = Connection(
            dbname = databaseConfig.dbname,
            port = databaseConfig.port,
            user = databaseConfig.user,
            password = databaseConfig.password,
            host = databaseConfig.host
        )

        connection.eventManager = eventManager
        connections[connectionName] = connection

        return connection
    }
}

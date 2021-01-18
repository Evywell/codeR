package fr.rob.game.infrastructure.database

import fr.rob.game.domain.event.EventManagerInterface
import fr.rob.game.infrastructure.config.database.DatabaseConfig

class ConnectionManager(private val eventManager: EventManagerInterface) {

    private val connections: MutableMap<String, Connection> = hashMapOf()

    fun getConnection(connectionName: String): Connection? = connections[connectionName]

    fun newConnection(connectionName: String, databaseConfig: DatabaseConfig) {
        val connection = Connection(
            dbname = databaseConfig.dbname,
            port = databaseConfig.port,
            user = databaseConfig.user,
            password = databaseConfig.password,
            host = databaseConfig.host
        )

        connection.eventManager = eventManager
        connections[connectionName] = connection
    }
}

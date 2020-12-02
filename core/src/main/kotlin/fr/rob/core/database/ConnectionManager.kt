package fr.rob.core.database

class ConnectionManager {

    private val connections: MutableMap<String, Connection> = hashMapOf()

    fun getConnection(connectionName: String): Connection? = connections[connectionName]

    fun newConnection(connectionName: String, databaseConfig: DatabaseConfig) {
        connections[connectionName] = Connection(
            dbname = databaseConfig.dbname,
            port = databaseConfig.port,
            user = databaseConfig.user,
            password = databaseConfig.password,
            host = databaseConfig.host
        )
    }
}
package fr.rob.core.database.pool

import fr.rob.core.config.database.DatabaseConfig
import fr.rob.core.database.ConnectionManager

class ConnectionPoolManager(private val slots: Int, private val connectionManager: ConnectionManager) {

    private val connectionPools: MutableMap<String, ConnectionPool> = HashMap()

    fun getPool(name: String): ConnectionPool? {
        return connectionPools[name]
    }

    fun createPool(name: String, config: DatabaseConfig): ConnectionPool {
        val pool = ConnectionPool(name, slots, connectionManager, config)

        connectionPools[name] = pool

        return pool
    }
}

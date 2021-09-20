package fr.rob.core.database.pool

import fr.rob.core.config.database.DatabaseConfig
import fr.rob.core.database.Connection
import fr.rob.core.database.ConnectionManager

class ConnectionPool(
    name: String,
    slots: Int,
    connectionManager: ConnectionManager,
    databaseConfig: DatabaseConfig
) {

    private val connectionSlots: Array<ConnectionSlot> = Array(slots) {
        ConnectionSlot(connectionManager.newConnection("$name-$it", databaseConfig))
    }

    fun getNextConnectionForHeavy(): Connection {
        return getNextConnection(3)
    }

    fun getNextConnection(weight: Int = 1): Connection {
        var bestSlot: ConnectionSlot? = null

        for (connectionSlot: ConnectionSlot in connectionSlots) {
            if (bestSlot == null || bestSlot.weight > connectionSlot.weight) {
                bestSlot = connectionSlot
            }
        }

        bestSlot!!.incrementWeight(weight)

        return bestSlot.connection
    }
}

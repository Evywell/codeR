package fr.rob.core.database.pool

import fr.rob.core.database.Connection

class ConnectionSlot(val connection: Connection) {

    var weight: Int = 0

    @Synchronized
    fun incrementWeight(amount: Int) {
        weight += amount
    }
}

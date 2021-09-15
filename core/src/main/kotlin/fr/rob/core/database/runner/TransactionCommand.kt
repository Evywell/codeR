package fr.rob.core.database.runner

import fr.rob.core.database.Connection
import java.sql.SQLException

class TransactionCommand(private val callable: () -> Unit, private val connection: Connection) : CommandInterface {

    override fun execute(): Any {
        try {
            connection.executeStatement("START TRANSACTION;")
            callable.invoke()
            connection.executeStatement("COMMIT;")
        } catch (e: SQLException) {
            connection.executeStatement("ROLLBACK;")

            throw e
        }

        return true
    }
}

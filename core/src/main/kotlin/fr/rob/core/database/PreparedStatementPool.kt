package fr.rob.core.database

import fr.rob.core.infrastructure.database.PreparedStatement
import java.sql.SQLException

class PreparedStatementPool(private val connection: Connection) : HashMap<String, PreparedStatement>() {

    fun getPreparedStatement(sql: String, returnKeys: Boolean): PreparedStatement {
        if (!this.containsKey(sql)) {
            val stmt: PreparedStatement = connection.createPreparedStatement(sql, returnKeys)
                ?: throw Exception("Cannot create sql statement: $sql")

            this[sql] = stmt
        }

        return this[sql]!!
    }

    fun close() {
        try {
            for ((_, value) in this) {
                value.close()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}

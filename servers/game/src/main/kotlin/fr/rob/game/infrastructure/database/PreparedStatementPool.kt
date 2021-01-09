package fr.rob.game.infrastructure.database

import java.lang.Exception
import java.sql.SQLException

class PreparedStatementPool(private val connection: Connection) : HashMap<String, PreparedStatement>() {

    fun getPreparedStatement(sql: String): PreparedStatement {
        if (!this.containsKey(sql)) {
            val stmt: PreparedStatement = connection.createPreparedStatement(sql)
                ?: throw Exception("Cannot create sql statement: $sql")

            this[sql] = stmt

            return stmt
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

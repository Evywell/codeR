package fr.rob.core.infrastructure.database

import fr.rob.core.database.event.AfterSQLReqExecutedEvent
import fr.rob.core.database.exception.InsertException
import fr.rob.core.misc.clock.StopWatch
import java.sql.*

class PreparedStatement(
    private val connection: fr.rob.core.database.Connection,
    private val sql: String,
    private val stmt: java.sql.PreparedStatement
) : java.sql.PreparedStatement by stmt {

    @Throws(InsertException::class)
    fun executeInsertOrThrow(exceptionMessage: String): Int {
        try {
            val res = executeUpdate()
            val rs = generatedKeys
            if (!rs.next()) {
                rs.close()
                throw InsertException(exceptionMessage)
            }
            rs.close()
            return res
        } catch (e: SQLException) {
            throw InsertException(e.message)
        }
    }

    override fun executeQuery(): ResultSet {
        val sw = StopWatch()
        sw.start()
        val rs = stmt.executeQuery()
        sw.stop()
        connection.triggerEvent(
            AfterSQLReqExecutedEvent.AFTER_SQL_REQ_EXECUTED_EVENT,
            AfterSQLReqExecutedEvent(sql, sw.diffTime())
        )
        return rs
    }

    override fun executeUpdate(): Int {
        val sw = StopWatch()
        sw.start()
        val affectedRows = stmt.executeUpdate()
        sw.stop()
        connection.triggerEvent(
            AfterSQLReqExecutedEvent.AFTER_SQL_REQ_EXECUTED_EVENT,
            AfterSQLReqExecutedEvent(sql, sw.diffTime())
        )
        return affectedRows
    }

    override fun execute(): Boolean {
        return try {
            stmt.execute()
        } catch (e: SQLException) {
            println("SQLException: ${e.message}")
            println("SQL Query: $sql")
            println("SQLState: ${e.sqlState}")
            println("VendorError: ${e.errorCode}")
            e.printStackTrace()
            throw e
        }
    }
}


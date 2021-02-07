package fr.rob.core.database

import fr.rob.core.database.event.AfterCreatePreparedStatementEvent
import fr.rob.core.event.EventInterface
import fr.rob.core.event.EventManagerInterface
import fr.rob.core.infrastructure.database.PreparedStatement
import fr.rob.core.misc.clock.StopWatch
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class Connection(
    var host: String,
    var port: Long,
    var user: String,
    var password: String,
    var dbname: String
) {

    lateinit var connection: Connection

    var connected: Boolean = false
    var pool = PreparedStatementPool(this)

    var eventManager: EventManagerInterface? = null

    constructor(dbname: String, user: String, password: String)
        : this("localhost", 3306, user, password, dbname)

    fun executeStatement(sql: String): Statement? {
        connect()
        try {
            val st = connection.createStatement()
            if (st.execute(sql)) {
                return st
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    fun createPreparedStatement(sql: String): PreparedStatement? {
        connect()

        try {
            val stopwatch = StopWatch()
            stopwatch.start()
            val stmt = PreparedStatement(this, sql, connection.prepareStatement(sql))
            stopwatch.stop()

            eventManager?.dispatch(AfterCreatePreparedStatementEvent(sql, stopwatch.diffTime()))

            return stmt
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return null
    }

    fun getPreparedStatement(sql: String): PreparedStatement {
        return pool.getPreparedStatement(sql)
    }

    fun triggerEvent(eventName: String, event: EventInterface) {
        eventManager?.dispatch(eventName, event)
    }

    private fun connect(): Boolean {
        if (connected) return true

        try {
            val dsn = String.format(
                "jdbc:mysql://%s:%d/%s?user=%s&password=%s&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC",
                host,
                port,
                dbname,
                user,
                password
            )

            this.connection = DriverManager.getConnection(
                dsn
            )
            connected = true
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun disconnect() {
        pool.close()
        try {
            this.connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

}

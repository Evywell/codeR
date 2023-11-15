package fr.rob.core.database

import fr.rob.core.database.event.AfterCreatePreparedStatementEvent
import fr.rob.core.database.runner.InsertStatementCommand
import fr.rob.core.database.runner.QueryRunner
import fr.rob.core.database.runner.StatementCommand
import fr.rob.core.database.runner.TransactionCommand
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
    var dbname: String,
) {
    private val queryRunner = QueryRunner()

    lateinit var connection: Connection
    var connected: Boolean = false
    var eventManager: EventManagerInterface? = null

    constructor(dbname: String, user: String, password: String) :
        this("localhost", 3306, user, password, dbname)

    /**
     * Returns true if the transaction is a success
     *
     * @throws SQLException otherwise
     */
    fun transaction(callable: () -> Unit): Boolean {
        connect()

        return queryRunner.executeCommand(TransactionCommand(callable, this)) as Boolean
    }

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

    fun createPreparedStatement(sql: String, returnKeys: Boolean = false): PreparedStatement? {
        connect()

        try {
            val stopwatch = StopWatch()
            stopwatch.start()

            val stmt: PreparedStatement = if (returnKeys) {
                PreparedStatement(
                    this,
                    sql,
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS),
                )
            } else {
                PreparedStatement(this, sql, connection.prepareStatement(sql))
            }

            stopwatch.stop()

            eventManager?.dispatch(AfterCreatePreparedStatementEvent(sql, stopwatch.diffTime()))

            return stmt
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return null
    }

    fun execute(stmt: PreparedStatement): Boolean {
        connect()

        return queryRunner.executeCommand(StatementCommand(stmt)) as Boolean
    }

    fun executeUpdate(stmt: PreparedStatement): Int {
        connect()

        return queryRunner.executeCommand(StatementCommand(stmt, true)) as Int
    }

    fun executeInsertOrThrow(stmt: PreparedStatement, message: String): Int {
        connect()

        return queryRunner.executeCommand(InsertStatementCommand(stmt, message)) as Int
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
                password,
            )

            this.connection = DriverManager.getConnection(
                dsn,
            )
            connected = true
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun disconnect() {
        try {
            this.connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}

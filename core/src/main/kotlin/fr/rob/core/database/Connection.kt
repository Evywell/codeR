package fr.rob.core.database

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
            return PreparedStatement(sql, connection.prepareStatement(sql))
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    fun getPreparedStatement(sql: String): PreparedStatement {
        return pool.getPreparedStatement(sql)
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
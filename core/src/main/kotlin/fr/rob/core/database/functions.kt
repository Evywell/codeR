package fr.rob.core.database

import fr.rob.core.infrastructure.database.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.Date

fun getIntAndClose(index: Int, resultSet: ResultSet, stmt: PreparedStatement? = null): Int =
    returnAndClose(resultSet.getInt(index), resultSet, stmt)

fun hasNextAndClose(resultSet: ResultSet, stmt: PreparedStatement? = null): Boolean =
    returnAndClose(resultSet.next(), resultSet, stmt)

fun getSQLNow(): Timestamp = dateToTimestamp(Date())

fun dateToTimestamp(date: Date): Timestamp = Timestamp(date.time)

fun <T : Any?> returnAndClose(variable: T, resultSet: ResultSet, stmt: PreparedStatement? = null): T {
    closeCursor(resultSet, stmt)

    return variable
}

fun <T : Any> returnAndCloseWithCallback(
    resultSet: ResultSet,
    preparedStatement: PreparedStatement,
    callable: () -> T
): T = returnAndClose(callable.invoke(), resultSet, preparedStatement)

fun getIntOrNull(resultSet: ResultSet, columnIndex: Int): Int? {
    val value = resultSet.getInt(columnIndex)

    if (resultSet.wasNull()) {
        return null
    }

    return value
}

fun getDateOrNull(resultSet: ResultSet, columnIndex: Int): Date? {
    val value = resultSet.getDate(columnIndex)

    if (resultSet.wasNull()) {
        return null
    }

    return value
}

fun closeCursor(resultSet: ResultSet, stmt: PreparedStatement? = null) {
    resultSet.close()
    stmt?.close()
}

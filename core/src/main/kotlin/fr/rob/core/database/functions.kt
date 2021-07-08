package fr.rob.core.database

import java.sql.ResultSet
import java.sql.Timestamp
import java.util.Date

fun getIntAndClose(index: Int, resultSet: ResultSet): Int {
    val value = resultSet.getInt(index)

    resultSet.close()

    return value
}

fun hasNextAndClose(resultSet: ResultSet): Boolean {
    val value = resultSet.next()

    resultSet.close()

    return value
}

fun getSQLNow(): Timestamp = dateToTimestamp(Date())

fun dateToTimestamp(date: Date): Timestamp = Timestamp(date.time)

fun <T : Any> returnAndClose(variable: T, resultSet: ResultSet): T {
    resultSet.close()

    return variable
}

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

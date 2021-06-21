package fr.rob.core.database

import java.sql.ResultSet
import java.sql.Timestamp

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

fun getSQLNow(): Timestamp {
    val datetime = java.util.Date()

    return Timestamp(datetime.time)
}

fun <T : Any> returnAndClose(variable: T, resultSet: ResultSet): T {
    resultSet.close()

    return variable
}

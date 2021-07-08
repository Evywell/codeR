package fr.rob.core.security

import fr.rob.core.database.Connection
import fr.rob.core.database.dateToTimestamp
import java.util.Date

class SecurityBanRepository(private val db: Connection) : SecurityBanRepositoryInterface {

    override fun insert(ip: String, service: String, endAt: Date, reason: String) {
        val stmt = db.getPreparedStatement(INS_SECURITY_BAN, true)

        stmt.setString(1, ip)
        stmt.setString(2, service)
        stmt.setTimestamp(3, dateToTimestamp(endAt))
        stmt.setString(4, reason)

        stmt.executeInsertOrThrow("Cannot insert security ban")
    }

    override fun byIp(ip: String): Ban? {
        val stmt = db.getPreparedStatement(SEL_BAN_BY_IP)

        stmt.setString(1, ip)
        stmt.execute()

        val rs = stmt.resultSet

        if (!rs.next()) {
            return null
        }

        return Ban(rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4))
    }

    companion object {
        const val SEL_BAN_BY_IP = "SELECT ip, service, end_at, reason FROM security_bans WHERE ip = ?;"

        const val INS_SECURITY_BAN = "INSERT INTO security_bans (ip, service, end_at, reason) VALUES (?, ?, ?, ?);"
    }
}

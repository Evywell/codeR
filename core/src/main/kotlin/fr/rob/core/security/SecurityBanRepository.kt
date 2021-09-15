package fr.rob.core.security

import fr.rob.core.database.Connection
import fr.rob.core.database.dateToTimestamp
import fr.rob.core.database.returnAndClose
import fr.rob.core.database.returnAndCloseWithCallback
import java.util.Date

class SecurityBanRepository(private val db: Connection) : SecurityBanRepositoryInterface {

    override fun insert(ip: String, service: String, endAt: Date, reason: String) {
        val stmt = db.createPreparedStatement(INS_SECURITY_BAN, true)!!

        stmt.setString(1, ip)
        stmt.setString(2, service)
        stmt.setTimestamp(3, dateToTimestamp(endAt))
        stmt.setString(4, reason)

        db.executeInsertOrThrow(stmt, "Cannot insert security ban")
	    stmt.close()
    }

    override fun byIp(ip: String): Ban? {
        val stmt = db.createPreparedStatement(SEL_BAN_BY_IP)!!

        stmt.setString(1, ip)
        db.execute(stmt)

        val rs = stmt.resultSet

        if (!rs.next()) {
            return returnAndClose(null, rs, stmt)
        }

        return returnAndCloseWithCallback(rs, stmt) {
            Ban(rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4))
        }
    }

    companion object {
        const val SEL_BAN_BY_IP = "SELECT ip, service, end_at, reason FROM security_bans WHERE ip = ?;"

        const val INS_SECURITY_BAN = "INSERT INTO security_bans (ip, service, end_at, reason) VALUES (?, ?, ?, ?);"
    }
}
